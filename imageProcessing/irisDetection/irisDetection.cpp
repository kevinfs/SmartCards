#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <iostream>
#include <cv.h>

using namespace cv;
using namespace std;

Mat createLUT(Mat image, Mat grad_x, Mat grad_y, int barX, int barY);

void barycentre(Mat grad, int *x, int *y);

void threshold(Mat frame);

int main(int argc, char **argv) {
    if (argc != 2) {
        cout << " Usage: irisDetection pathToImage" << endl;
        return -1;
    }

    Mat originalImage;
    originalImage = imread(argv[1], CV_LOAD_IMAGE_COLOR);   // Read the file
    Mat greyImage;
    cvtColor(originalImage, greyImage, CV_RGB2GRAY);

    if (!originalImage.data)                              // Check for invalid input
    {
        cout << "Could not open or find the image" << endl;
        return -1;
    }

    int scale = 1;
    int delta = 0;
    int ddepth = CV_16S;

    Mat grad;

    // Sobel
    /// Generate grad_x and grad_y
    Mat grad_x, grad_y;
    Mat abs_grad_x, abs_grad_y;

    /// Gradient X
    Sobel(greyImage, grad_x, ddepth, 1, 0, 3, scale, delta, BORDER_DEFAULT);
    convertScaleAbs(grad_x, abs_grad_x);

    /// Gradient Y
    Sobel(greyImage, grad_y, ddepth, 0, 1, 3, scale, delta, BORDER_DEFAULT);
    convertScaleAbs(grad_y, abs_grad_y);

    /// Total Gradient (approximate)
    addWeighted(abs_grad_x, 0.5, abs_grad_y, 0.5, 0, grad);

    imshow("Sobel", grad);

    threshold(grad);

    int barX, barY;
    barycentre(grad, &barX, &barY);

    cout << barX << " " << barY << endl;

    // Draw a circle
    circle(originalImage, Point(barX, barY), 1, Scalar(0, 0, 255), 5, 8);
    imshow("Original Image", originalImage);

    // Create LUT
    Mat lut = createLUT(grad, grad_x, grad_y, barX, barY);

    waitKey(0);
    return 0;
}

// TODO
Mat createLUT(Mat image, Mat grad_x, Mat grad_y, int barX, int barY) {
    Mat lut = Mat::zeros(2, 180, CV_8UC3);

    for (int i = 1; i < image.rows - 1; i++) {
        for (int j = 1; j < image.cols - 1; j++) {

        }
    }
}

void barycentre(Mat grad, int *x, int *y) {
    int sumX = 0, sumY = 0;
    int barX = 0, barY = 0;
    for (int i = 1; i < grad.rows - 1; i++) {
        for (int j = 1; j < grad.cols - 1; j++) {
            Vec3b &color = grad.at<Vec3b>(i, j);
            int b = color[0]; /* le B */
            int g = color[1]; /* le G */
            int r = color[2]; /* le R */
            if (r > 0 || g > 0 || b > 0) {
                barX += i;
                barY += j;

                sumX++;
                sumY++;
            }
        }
    }

    *x = barX / sumX;
    *y = barY / sumY;
}

void threshold(Mat frame) {
    for (int i = 1; i < frame.rows - 1; i++) {
        for (int j = 1; j < frame.cols - 1; j++) {

            Vec3b &color = frame.at<Vec3b>(i, j);

            int b = color[0]; /* le B */
            int g = color[1]; /* le G */
            int r = color[2]; /* le R */
//    cout << r << " " << g << " " << b << " " << endl;

            if (r > 0 || g > 0 || b > 0) {
                color[0] = 255; /* le B */
                color[1] = 255; /* le G */
                color[2] = 255; /* le R */
            }
        }
    }
}
