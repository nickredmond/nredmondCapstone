package stuff;

import java.util.Random;

import org.jocl.CL;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_context_properties;
import org.jocl.cl_device_id;
import org.jocl.cl_kernel;
import org.jocl.cl_mem;
import org.jocl.cl_platform_id;
import org.jocl.cl_program;

public class GpuMain {
	
	private static String programString =
			 "__kernel void "+
		        "sampleKernel(__global const float *a,"+
		        "             __global float *c)"+
		        "{"+
		        "    c[0] = 1.0f / (1.0f + exp(a[0]));"+
		        "}";

	public static void main(String[] args) {		
		cl_platform_id[] platforms = new cl_platform_id[1];
		CL.clGetPlatformIDs(platforms.length, platforms, null);
		
		cl_context_properties contextProps = new cl_context_properties();
		contextProps.addProperty(CL.CL_CONTEXT_PLATFORM, platforms[0]);
		
		cl_context gpuContext = CL.clCreateContextFromType(contextProps, CL.CL_DEVICE_TYPE_GPU, null, null, null);
		
		if (gpuContext == null){
			System.out.println("NO GPU CONTEXT AVAILABLE");
		}
		
		CL.setExceptionsEnabled(true);
		
		long[] numBytes = new long[1];
		CL.clGetContextInfo(gpuContext, CL.CL_CONTEXT_DEVICES, 0, null, numBytes);
		int numDevices = (int)numBytes[0] / Sizeof.cl_device_id;
		
		System.out.println("Number of GPUs: " + numDevices);
		
		cl_device_id[] devices = new cl_device_id[numDevices];
		CL.clGetContextInfo(gpuContext, CL.CL_CONTEXT_DEVICES, numBytes[0], Pointer.to(devices), null);
		
		Random rand = new Random();
		
		long start, end;
		long gpuTime;
		long cpuTime;
		
		start = System.nanoTime();
		for (int i = 0; i < 100; i++){
			float exp = 1.0f / (1.0f + (float)Math.exp(rand.nextInt(100) - 50));
		}
		end = System.nanoTime();
		gpuTime = end - start;
		
		start = System.nanoTime();
		for (int i = 0; i < 100; i++){
			gpuExp((float)rand.nextInt(100) - 50, gpuContext, devices);
		}
		end = System.nanoTime();
		cpuTime = end - start;
		
		CL.clReleaseContext(gpuContext);
		System.out.println("GPU time: " + gpuTime);
		System.out.println("CPU time: " + cpuTime);
	}

	private static float gpuExp(float value, cl_context gpuContext, cl_device_id[] devices){
		float[] input = {value};
		float[] output = {0.0f};
		Pointer srcIn = Pointer.to(input);
		Pointer srcOut = Pointer.to(output);
		
		cl_command_queue commandQueue = CL.clCreateCommandQueue(gpuContext, devices[0], 0, null);
		cl_mem[] memObjects = new cl_mem[2];
		memObjects[0] = CL.clCreateBuffer(gpuContext, CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR,
				Sizeof.cl_float, srcIn, null);
		memObjects[1] = CL.clCreateBuffer(gpuContext, CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR,
				Sizeof.cl_float, srcOut, null);
		
		cl_program program = CL.clCreateProgramWithSource(gpuContext, 1, new String[]{programString}, null, null);
		CL.clBuildProgram(program, 0, null, null, null, null);
		cl_kernel kernel = CL.clCreateKernel(program, "sampleKernel", null);
		
		CL.clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memObjects[0]));
		CL.clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memObjects[1]));
		
		long[] global_work_size = new long[]{1};
		long[] local_work_size = new long[]{1};
		
		CL.clEnqueueNDRangeKernel(commandQueue, kernel, 1, null, global_work_size, local_work_size,
				0, null, null);
		CL.clEnqueueReadBuffer(commandQueue, memObjects[1], CL.CL_TRUE, 0, Sizeof.cl_float, srcOut, 0, null, null);
		
		CL.clReleaseMemObject(memObjects[0]);
		CL.clReleaseMemObject(memObjects[1]);
		CL.clReleaseKernel(kernel);
		CL.clReleaseProgram(program);
		CL.clReleaseCommandQueue(commandQueue);
		
		float result = output[0];
		return result;
	}
}