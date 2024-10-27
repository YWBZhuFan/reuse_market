package com.zyh.market.utils;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class QRCodeUtil {

    /**
     * 生成带背景图片带圆形logo的二维码
     *
     * @param content  二维码内容
     * @param logoPath logo图片地址
     * @param bgPath   背景图片地址
     * @param text     下发描述文字
     * @param savePath 生成图片的保存地址
     */
    public static void LogoBackgroundQrCode(String content, String logoPath, String bgPath, String text, String savePath) {
        BufferedImage image = generateQrCode(content);
        BufferedImage QcLogoCode = insertLogoImage(image, logoPath);
        createPicture(QcLogoCode, bgPath, text, savePath);
    }

    public static File LogoQrCode(String url, String logoPath, String fileName) throws IOException {
        BufferedImage image = generateQrCode(url);
        BufferedImage imageLog = insertLogoImage(image, logoPath);
        File tempFile = File.createTempFile(fileName, ".png");
        ImageIO.write(imageLog, "png", tempFile);
        return tempFile;
    }

    /**
     * 生成中间带logo的二维码
     *
     * @param content 二维码内容
     * @return
     */
    public static BufferedImage generateQrCode(String content) {
        BufferedImage image = QrCodeUtil.generate(
                content, // 二维码内容
                QrConfig.create()
                        .setErrorCorrection(ErrorCorrectionLevel.H) //纠错级别使用zxing的ErrorCorrectionLevel枚举封装，包括：L、M、Q、H几个参数，由低到高。低级别的像素块更大
                        .setWidth(300) // 二维码的宽
                        .setHeight(300) // 二维码的高
                        .setMargin(0)); // 边距
        return image;
    }

    /**
     * 在二维码上画上方形logo图标
     *
     * @param image
     * @return
     * @throws Exception
     */
    public static BufferedImage insertLogoImage(BufferedImage image, String logoPath) {
        try {
            Image src = ImageIO.read(new File(logoPath));
            // 插入LOGO
            Graphics2D graph = image.createGraphics();
            // 定义一个正方形，其左上角位于 (110, 110)，宽度和高度都是 70 像素
            Rectangle2D shape = new Rectangle2D.Double(110, 110, 70, 70);
            // 需要保留的区域
            graph.setClip(shape);
            graph.drawImage(src, 110, 110, 70, 70, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 带文字和背景图片的二维码创建
     *
     * @param image    生成的二维码
     * @param bgPath   背景图片的路径
     * @param text     二维码上的文字
     * @param savePath 生成图片的保存位置
     * @throws IOException
     */
    public static void createPicture(BufferedImage image, String bgPath, String text, String savePath) {
        try {
            // 首先先画背景图片
            BufferedImage backgroundImage = ImageIO.read(new File(bgPath));
            // 背景图片的宽度
            int bgWidth = backgroundImage.getWidth();
            // 二维码的宽度
            int qrWidth = image.getWidth();
            // 二维码距离背景图片横坐标（X）的距离，居中显示
            int distanceX = (bgWidth - qrWidth) / 2;
            // 二维码距离背景图片纵坐标（Y）的距离
            int distanceY = distanceX;
            // 基于图片backgroundImage对象打开绘图
            Graphics2D rng = backgroundImage.createGraphics();
            rng.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP));
            rng.drawImage(image, distanceX, distanceY, null);

            //设置字体
            Font font = new Font("宋体", Font.PLAIN, 22);
            rng.setFont(font);
            rng.setColor(Color.red);
            // 获取当前文字的对象
            FontMetrics metrics = rng.getFontMetrics(font);
            // 文字在图片中的坐标 这里设置在中间
            int startX = (bgWidth - metrics.stringWidth(text)) / 2; //当前文字对象到横坐标（X）的距离
            int startY = backgroundImage.getHeight() - 60; //当前文字对象到纵坐标（Y）的距离
            rng.drawString(text, startX, startY);
            // 处理绘图
            rng.dispose();
            image = backgroundImage;
            image.flush();
            // 将绘制好的图片写入当前路径中
            ImageIO.write(image, "png", new File(savePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}