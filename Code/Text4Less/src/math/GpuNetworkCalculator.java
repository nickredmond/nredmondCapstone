package math;

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

public class GpuNetworkCalculator implements INetworkCalculator {
	private static final String SIGMOID_SOURCE = "__kernel void "+
	        "sigmoidKernel(__global const float *input,"+
	        "             __global float *output)"+
	        "{"+
	        "	 int globalId = get_global_id(0);"+
	        "    output[globalId] = 1.0f / (1.0f + exp(input[globalId]));"+
	        "}";
	private static final String ERROR_SOURCE = "__kernel void"+
	        "errorKernel(__global const float *weightValues,"+
			"			 __global const float *nextError,"+
	        "			 __global float *error"+	
	        "{"+
			"    int globalId = get_global_id(0);"+
	        "	 error[globalId] = weightValues[globalId] + nextError[globalId];";

	private cl_context gpuContext;
	private cl_device_id[] devices;
	
	public GpuNetworkCalculator(){
		cl_platform_id[] platforms = new cl_platform_id[1];
		CL.clGetPlatformIDs(platforms.length, platforms, null);
		
		cl_context_properties contextProps = new cl_context_properties();
		contextProps.addProperty(CL.CL_CONTEXT_PLATFORM, platforms[0]);
		
		gpuContext = CL.clCreateContextFromType(contextProps, CL.CL_DEVICE_TYPE_GPU, null, null, null);
		
		if (gpuContext == null){
			System.out.println(("COULD NOT ACCESS GPU"));
		}
		
		CL.setExceptionsEnabled(true);
		
		long[] numBytes = new long[1];
		CL.clGetContextInfo(gpuContext, CL.CL_CONTEXT_DEVICES, 0, null, numBytes);
		int numDevices = (int)numBytes[0] / Sizeof.cl_device_id;
		
		devices = new cl_device_id[numDevices];
		CL.clGetContextInfo(gpuContext, CL.CL_CONTEXT_DEVICES, numBytes[0], Pointer.to(devices), null);
	}
	
	@Override
	public void close(){
		CL.clReleaseContext(gpuContext);
	}
	
	@Override
	public float[] calculateSigmoidValues(float[] zValues) {
		int length = zValues.length;
		float[] input = zValues;
		float[] output = new float[zValues.length];
		Pointer srcIn = Pointer.to(input);
		Pointer srcOut = Pointer.to(output);
		
		cl_command_queue commandQueue = CL.clCreateCommandQueue(gpuContext, devices[0], 0, null);
		cl_mem[] memObjects = new cl_mem[2];
		memObjects[0] = CL.clCreateBuffer(gpuContext, CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR,
				Sizeof.cl_float * length, srcIn, null);
		memObjects[1] = CL.clCreateBuffer(gpuContext, CL.CL_MEM_READ_WRITE,
				Sizeof.cl_float * length, srcOut, null);
		
		cl_program program = CL.clCreateProgramWithSource(gpuContext, 1, new String[]{SIGMOID_SOURCE}, null, null);
		CL.clBuildProgram(program, 0, null, null, null, null);
		cl_kernel kernel = CL.clCreateKernel(program, "sigmoidKernel", null);
		
		CL.clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memObjects[0]));
		CL.clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memObjects[1]));
		
		long[] global_work_size = new long[]{length};
		long[] local_work_size = new long[]{1};
		
		CL.clEnqueueNDRangeKernel(commandQueue, kernel, 1, null, global_work_size, local_work_size,
				0, null, null);
		CL.clEnqueueReadBuffer(commandQueue, memObjects[1], CL.CL_TRUE, 0, Sizeof.cl_float * length, srcOut, 0, null, null);
		
		CL.clReleaseMemObject(memObjects[0]);
		CL.clReleaseMemObject(memObjects[1]);
		CL.clReleaseKernel(kernel);
		CL.clReleaseProgram(program);
		CL.clReleaseCommandQueue(commandQueue);

		return output;
	}

	@Override
	public float[] calculateErrorValues(float[][] weights,
			float[] nextLayerError) {
		// TODO Auto-generated method stub
		return null;
	}

}
