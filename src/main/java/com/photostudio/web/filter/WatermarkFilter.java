package com.photostudio.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;

@WebFilter(urlPatterns = {"/photo/*"})
@Slf4j
public class WatermarkFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig)  {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("watermark filter");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        OrigResponseWrapper origResponseWrapper = new OrigResponseWrapper(httpServletResponse);
        chain.doFilter(httpServletRequest, origResponseWrapper);
        log.info("add watermark");
        if (origResponseWrapper.printWriter != null) {
            origResponseWrapper.printWriter.flush();
        }
        origResponseWrapper.servletOutputStream.flush();

        byte[] imageData = origResponseWrapper.byteArrayOutputStream.toByteArray();
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageData));
        addWatermark(bufferedImage, "MIARIFOTOGRAFIE");

        ImageIO.write(bufferedImage, "PNG", httpServletResponse.getOutputStream());
        log.info("added watermark");
    }

    private void addWatermark(BufferedImage originalImage, String watermarkText) {
        Graphics2D graphics2D = originalImage.createGraphics();
        graphics2D.scale(1, 1);
        graphics2D.addRenderingHints(
                new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON));
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Font font = new Font(Font.MONOSPACED, Font.BOLD, 60);
        GlyphVector fontGV = font.createGlyphVector(graphics2D.getFontRenderContext(), watermarkText);
        Rectangle rectangle = fontGV.getPixelBounds(graphics2D.getFontRenderContext(), 0, 0);
        Shape textShape = fontGV.getOutline();
        double textWidth = rectangle.getWidth();
        double textHeight = rectangle.getHeight();
        double rotateAngle = -Math.PI / 25d;
        AffineTransform rotate45 = AffineTransform.getRotateInstance(rotateAngle);
        Shape rotatedTextShape = rotate45.createTransformedShape(textShape);

        graphics2D.setPaint(new GradientPaint(0, 0,
                new Color(1f, 1f, 1f, 0.1f),
                originalImage.getWidth() / 2, originalImage.getHeight() / 2,
                new Color(1f, 1f, 1f, 0.2f)));

        graphics2D.setStroke(new BasicStroke(0.8f));

        double yStep = Math.abs(textWidth * Math.sin(rotateAngle)) * 5 + 20;
        double xStep = Math.abs(textWidth * Math.cos(rotateAngle)) + 20;

        int i = 0;
        double y;
        double nextY;
        for (double x = -textHeight * 3; x < originalImage.getWidth(); x += xStep) {
            y = (i % 2 == 1) ? -yStep / 2 : -yStep;
            for (; y < originalImage.getHeight(); y += yStep) {
                graphics2D.draw(rotatedTextShape);
                graphics2D.fill(rotatedTextShape);
                graphics2D.translate(0, yStep);
            }
            i++;
            nextY = (i % 2 == 1) ? y + yStep / 2 : y + yStep;
            graphics2D.translate(xStep, -nextY);
        }
    }

    private class OrigResponseWrapper extends HttpServletResponseWrapper {
        protected final HttpServletResponse origResponse;
        protected ServletOutputStream servletOutputStream = null;
        protected ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        protected PrintWriter printWriter = null;

        public OrigResponseWrapper(HttpServletResponse response) {
            super(response);
            origResponse = response;
        }

        public ServletOutputStream createOutputStream()  {
            return servletOutputStream == null ? new ServletOutputStream() {
                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setWriteListener(WriteListener writeListener) {

                }

                @Override
                public void write(int b)  {
                    byteArrayOutputStream.write(b);
                }
            } : servletOutputStream;
        }

        @Override
        public ServletOutputStream getOutputStream() {
            if (servletOutputStream == null) {
                servletOutputStream = createOutputStream();
            }
            return servletOutputStream;
        }

        @Override
        public PrintWriter getWriter()  {
            servletOutputStream = getOutputStream();
            if (printWriter == null) {
                printWriter = new PrintWriter(servletOutputStream);
            }
            return printWriter;
        }
    }
}
