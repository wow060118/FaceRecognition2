package com.facerecognition.utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class OpenCVUtils {
	/**
	 * 
	 * 用直方图来比较这两个图像。 Compare that two images is similar using histogram
	 * 
	 * @author minikim
	 * @param img1
	 *            - the first image
	 * @param img2
	 *            - the second image
	 * @return integer - 1 if two images are similar, 0 if not
	 */
	public static int compareHistogram(Mat img1, Mat img2) {
		int retVal = 0;
		if (null != img1 && null != img2) {
			// 加载图片
			// Mat img1 = Imgcodecs.imread(filename1,
			// Imgcodecs.CV_LOAD_IMAGE_COLOR);
			// Mat img2 = Imgcodecs.imread(filename2,
			// Imgcodecs.CV_LOAD_IMAGE_COLOR);
//			Mat imdecode = Imgcodecs.imdecode(img1, Imgcodecs.CV_LOAD_IMAGE_COLOR);
//			Mat imdecode2 = Imgcodecs.imdecode(img2, Imgcodecs.CV_LOAD_IMAGE_COLOR);
			Mat hsvImg1 = new Mat();
			Mat hsvImg2 = new Mat();
			// Convert to HSV 转换为彩色模型
			Imgproc.cvtColor(img1, hsvImg1, Imgproc.COLOR_BGR2HSV);
			Imgproc.cvtColor(img2, hsvImg2, Imgproc.COLOR_BGR2HSV);
			// Set configuration for calchist()
			List<Mat> listImg1 = new ArrayList<Mat>();
			List<Mat> listImg2 = new ArrayList<Mat>();
			listImg1.add(hsvImg1);
			listImg2.add(hsvImg2);
			MatOfFloat ranges = new MatOfFloat(0, 255);
			MatOfInt histSize = new MatOfInt(50);
			MatOfInt channels = new MatOfInt(0);
			// Histograms 直方图
			Mat histImg1 = new Mat();
			Mat histImg2 = new Mat();
			// Calculate the histogram for the HSV imgaes
			// 计算HSV imgaes的直方图。
			Imgproc.calcHist(listImg1, channels, new Mat(), histImg1, histSize, ranges);
			Imgproc.calcHist(listImg2, channels, new Mat(), histImg2, histSize, ranges);
			Core.normalize(histImg1, histImg1, 0, 1, Core.NORM_MINMAX, -1, new Mat());
			Core.normalize(histImg2, histImg2, 0, 1, Core.NORM_MINMAX, -1, new Mat());
			// Apply the histogram comparison methods
			// 0 - correlation: the higher the metric, the more accurate the
			// match
			// "> 0.9"
			// 1 - chi-square: the lower the metric, the more accurate the match
			// "<
			// 0.1"
			// 2 - intersection: the higher the metric, the more accurate the
			// match
			// "> 1.5"
			// 3 - bhattacharyya: the lower the metric, the more accurate the
			// match
			// "< 0.3"
			double result0, result1, result2, result3;
			result0 = Imgproc.compareHist(histImg1, histImg2, 0);
			result1 = Imgproc.compareHist(histImg1, histImg2, 1);
			result2 = Imgproc.compareHist(histImg1, histImg2, 2);
			result3 = Imgproc.compareHist(histImg1, histImg2, 3);
			// If the count that it is satisfied with the condition is over 3,
			// two
			// images is same.
			int count = 0;
			if (result0 > 0.9) {
				count++;
			}
			if (result1 < 0.1) {
				count++;
			}
			if (result2 > 1.5) {
				count++;
			}
			if (result3 < 0.3) {
				count++;
			}
			if (count >= 3) { // 对相似度进行评判
				retVal = 1;
			}
		}

		return retVal;
	}

	/**
	 * 将base64转换为mat
	 * 
	 * @param base64Img
	 * @return
	 * @throws IOException
	 */
	public static Mat base64ToMat(String base64Img) throws IOException {
		Mat mat = null;
		if (null != base64Img && base64Img != "") {
			BASE64Decoder decoder = new BASE64Decoder();
			// 将base64转换为字节数组
			byte[] bytes = decoder.decodeBuffer(base64Img);
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			BufferedImage read = ImageIO.read(bais);
			byte[] pixels = ((DataBufferByte) read.getRaster().getDataBuffer()).getData();
			mat = Mat.eye(read.getHeight(), read.getWidth(), CvType.CV_8UC3);
			mat.put(0, 0, pixels);
		}
		return mat;
	}

	/**
	 * 解析人脸截取识别的人脸
	 * 
	 * @param request
	 * @param base64img
	 * @return
	 * @throws IOException
	 */
	public static Mat analysisFace(String base64img, CascadeClassifier faceDetector) throws IOException {
		Mat mat = null;
		Mat image = base64ToMat(base64img);
		if (null != image) {
			// 在图片中检测人脸
			MatOfRect faceDetections = new MatOfRect();
			// 开始解析
			faceDetector.detectMultiScale(image, faceDetections);
			Rect[] rects = faceDetections.toArray();
			if (rects != null && rects.length >= 1) {
				// 画出矩形
				Rect rect = rects[0]; //只获取一个人脸
				Mat mat2 = new Mat(image, new Rect(rect.x, rect.y, rect.width, rect.height));
				mat = mat2;
			}
			
		}
		return mat;
	}

	/**
	 * 将mat转换为base64
	 * 
	 * @param img
	 * @return
	 * @throws IOException
	 */
	public static String matToBase64(Mat img) throws IOException {
		String pngBase64 = "";
		if (img != null) {
			MatOfByte mob = new MatOfByte();
			Imgcodecs.imencode(".jpg", img, mob);
			byte[] byteArray = mob.toArray();
			InputStream in = new ByteArrayInputStream(byteArray);
			BufferedImage bufImage = ImageIO.read(in);
			// io流
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// 写入流中
			ImageIO.write(bufImage, "jpg", baos);
			// 转换成字节
			byte[] bytes = baos.toByteArray();
			BASE64Encoder encoder = new BASE64Encoder();
			// 转换成base64串
			pngBase64 = encoder.encodeBuffer(bytes).trim();
			// 删除\r\n
			pngBase64 = pngBase64.replaceAll("\n", "").replaceAll("\r", "");
		}
		return pngBase64;
	}

	/**
	 * 将base64编码转换为byte[]
	 * 
	 * @param base64Img
	 * @return
	 * @throws Exception
	 */
	public static byte[] base642byte(String base64Img) throws IOException {
		byte[] bytes = null;
		if (null != base64Img && base64Img != "") {
			BASE64Decoder decoder = new BASE64Decoder();
			// 将base64转换为字节数组
			bytes = decoder.decodeBuffer(base64Img);
		}
		return bytes;
	}

	/**
	 * 将byte数组转换为Mat
	 * 
	 * @param imgbyte
	 *            byte[]
	 * @return
	 * @throws IOException
	 */
	public static Mat byte2Mat(byte[] imgbyte) throws IOException {
		Mat mat = null;
		ByteArrayInputStream bais = new ByteArrayInputStream(imgbyte);
		BufferedImage read = ImageIO.read(bais);
		byte[] pixels = ((DataBufferByte) read.getRaster().getDataBuffer()).getData();
		mat = Mat.eye(read.getHeight(), read.getWidth(), CvType.CV_8UC3);
		mat.put(0, 0, pixels);
		return mat;
	}
}
