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
		        "			  __global const float *b,"+
		        "             __global float *c)"+
		        "{"+
		        "	 int globalId = get_global_id(0);"+
		        "    c[globalId] = a[globalId] * b[globalId];"+
		        "}";

	private static long actualTime = 0;
	
	public static void main(String[] args) throws Exception {		
		cl_platform_id[] platforms = new cl_platform_id[1];
		CL.clGetPlatformIDs(platforms.length, platforms, null);
		
		cl_context_properties contextProps = new cl_context_properties();
		contextProps.addProperty(CL.CL_CONTEXT_PLATFORM, platforms[0]);
		
		cl_context gpuContext = CL.clCreateContextFromType(contextProps, CL.CL_DEVICE_TYPE_GPU, null, null, null);
		
		if (gpuContext == null){
			System.out.println(("COULD NOT ACCESS GPU"));
		}
		
		CL.setExceptionsEnabled(true);
		
		long[] numBytes = new long[1];
		CL.clGetContextInfo(gpuContext, CL.CL_CONTEXT_DEVICES, 0, null, numBytes);
		int numDevices = (int)numBytes[0] / Sizeof.cl_device_id;
		
		System.out.println("Number of GPUs: " + numDevices);
		
		cl_device_id[] devices = new cl_device_id[numDevices];
		CL.clGetContextInfo(gpuContext, CL.CL_CONTEXT_DEVICES, numBytes[0], Pointer.to(devices), null);
		
		cl_command_queue commandQueue = CL.clCreateCommandQueue(gpuContext, devices[0], 0, null);
		
		cl_program program = CL.clCreateProgramWithSource(gpuContext, 1, new String[]{programString}, null, null);
		CL.clBuildProgram(program, 0, null, null, null, null);
		cl_kernel kernel = CL.clCreateKernel(program, "sampleKernel", null);
		
		Random rand = new Random();
		
		long start, end;
		long gpuTime;
		long cpuTime;
		
		System.out.println("started gpu");
		
		int length = 100;
		
		float[] stuff1 = new float[length];
		float[] stuff2 = new float[length];
		
		for (int j = 0; j < stuff1.length; j++){
			stuff1[j] = rand.nextFloat() * 2 - 1;
			stuff2[j] = rand.nextFloat() * 2 - 1;
		}
		
		start = System.nanoTime();
		for (int i = 0; i < length; i++){
			
			gpuExp(stuff1, stuff2, gpuContext, devices, commandQueue, kernel);
		}
		end = System.nanoTime();
		gpuTime = end - start;
		
		System.out.println("finished gpu");
		
		start = System.nanoTime();
		for (int i = 0; i < length; i++){
			float[] output = new float[length];
			
			for (int j = 0; j < output.length; j++){
				output[j] = stuff1[j] * stuff2[j];
			}
		}
		end = System.nanoTime();
		cpuTime = end - start;
		
		CL.clReleaseKernel(kernel);
		CL.clReleaseProgram(program);
		CL.clReleaseCommandQueue(commandQueue);
		CL.clReleaseContext(gpuContext);
		System.out.println("GPU time: " + gpuTime);
		System.out.println("GPU calculation time: " + actualTime);
		System.out.println("GPU memory allocation time: " + (gpuTime - actualTime));
		System.out.println("CPU time: " + cpuTime);
	}

	private static float[] gpuExp(float[] values1, float[] values2, cl_context gpuContext, cl_device_id[] devices, cl_command_queue commandQueue,
			cl_kernel kernel){
		float[] output = new float[values1.length];
		Pointer srcIn1 = Pointer.to(values1);
		Pointer srcIn2 = Pointer.to(values2);
		Pointer srcOut = Pointer.to(output);
		
		cl_mem[] memObjects = new cl_mem[3];
		memObjects[0] = CL.clCreateBuffer(gpuContext, CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR,
				Sizeof.cl_float * values1.length, srcIn1, null);
		memObjects[1] = CL.clCreateBuffer(gpuContext, CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR,
				Sizeof.cl_float * values2.length, srcIn2, null);
		memObjects[2] = CL.clCreateBuffer(gpuContext, CL.CL_MEM_READ_WRITE,
				Sizeof.cl_float * output.length, srcOut, null);
		
		CL.clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memObjects[0]));
		CL.clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memObjects[1]));
		CL.clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(memObjects[2]));
		
		long[] global_work_size = new long[]{values1.length};
		long[] local_work_size = new long[]{1};
		
		long start = System.nanoTime();
		CL.clEnqueueNDRangeKernel(commandQueue, kernel, 1, null, global_work_size, local_work_size,
				0, null, null);
		CL.clEnqueueReadBuffer(commandQueue, memObjects[2], CL.CL_TRUE, 0, Sizeof.cl_float * output.length, srcOut, 0, null, null);
		long end = System.nanoTime();
		
		actualTime += (end - start);
		
		CL.clReleaseMemObject(memObjects[0]);
		CL.clReleaseMemObject(memObjects[1]);
		CL.clReleaseMemObject(memObjects[2]);
		
		return output;
	}
}
