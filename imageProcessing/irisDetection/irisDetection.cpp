#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <iostream>
#include <cv.h>

using namespace cv;
using namespace std;

Mat createLUT(Mat image, Mat grad_x, Mat grad_y, int barX, int barY);


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

void seuillage(Mat frame, int x, int y) {
    Vec3b &color = frame.at<Vec3b>(x, y);

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

    namedWindow("Original", WINDOW_AUTOSIZE);
    imshow("Original", originalImage);

    namedWindow("Grey", WINDOW_AUTOSIZE);
    imshow("Grey", greyImage);

    int scale = 1;
    int delta = 0;
    int ddepth = CV_16S;
    int framecount = 0;

    Mat grad;

    // Sobel
    /// Generate grad_x and grad_y
    Mat grad_x, grad_y;
    Mat abs_grad_x, abs_grad_y;

    /// Gradient X
    //Scharr( src_gray, grad_x, ddepth, 1, 0, scale, delta, BORDER_DEFAULT );
    Sobel(greyImage, grad_x, ddepth, 1, 0, 3, scale, delta, BORDER_DEFAULT);
    convertScaleAbs(grad_x, abs_grad_x);

    /// Gradient Y
    //Scharr( src_gray, grad_y, ddepth, 0, 1, scale, delta, BORDER_DEFAULT );
    Sobel(greyImage, grad_y, ddepth, 0, 1, 3, scale, delta, BORDER_DEFAULT);
    convertScaleAbs(grad_y, abs_grad_y);

    /// Total Gradient (approximate)
    addWeighted(abs_grad_x, 0.5, abs_grad_y, 0.5, 0, grad);

    for (int i = 1; i < grad.rows - 1; i++) {
        for (int j = 1; j < grad.cols - 1; j++) {
            seuillage(grad, i, j);
        }
    }

    int x, y;
    barycentre(grad, &x, &y);

    cout << x << " " << y << endl;

    // Draw a circle
    circle(grad, Point(x, y), 32.0, Scalar(0, 0, 255), 1, 8);
    Mat lut = createLUT(grad, grad_x, grad_y, x, y);
    imshow("Image", grad);
    waitKey(0);
    return 0;
}

Mat createLUT(Mat image, Mat grad_x, Mat grad_y, int barX, int barY) {
    Mat lut = Mat::zeros(2, 180, CV_8UC3);


    for (int i = 1; i < image.rows - 1; i++) {
        for (int j = 1; j < image.cols - 1; j++) {

        }
    }

}

