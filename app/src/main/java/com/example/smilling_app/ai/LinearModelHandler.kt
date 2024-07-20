package com.example.smilling_app.ai

import android.content.Context
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class LinearModelHandler(context: Context) {

    private var interpreter: Interpreter

    init {
        interpreter = Interpreter(loadModelFile(context))
    }

    @Throws(IOException::class)
    private fun loadModelFile(context: Context): MappedByteBuffer {
        val assetFileDescriptor = context.assets.openFd("linear_model.tflite")
        val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = fileInputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun predict(inputData: FloatArray): FloatArray {
        val inputFeature = TensorBuffer.createFixedSize(intArrayOf(1, 5), DataType.FLOAT32)
        val inputBuffer = ByteBuffer.allocateDirect(4 * inputData.size)
        inputBuffer.order(ByteOrder.nativeOrder())
        inputBuffer.asFloatBuffer().put(inputData)
        inputFeature.loadBuffer(inputBuffer)

        val outputFeature = TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)

        interpreter.run(inputFeature.buffer, outputFeature.buffer.rewind())

        return outputFeature.floatArray
    }

    fun close() {
        interpreter.close()
    }
}