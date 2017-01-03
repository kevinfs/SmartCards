#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <iostream>

using namespace cv;
using namespace std;

int main( int argc, char** argv )
{
    if(argc != 2)
    {
     cout <<" Usage: irisDetection pathToImage" << endl;
     return -1;
    }

    Mat originalImage;
    originalImage = imread(argv[1], CV_LOAD_IMAGE_COLOR);   // Read the file
    Mat greyImage;
    cvtColor(originalImage, greyImage, CV_RGB2GRAY);

    if(!originalImage.data)                              // Check for invalid input
    {
        cout << "Could not open or find the image" << endl ;
        return -1;
    }

    namedWindow("Original", WINDOW_AUTOSIZE);
    imshow("Original", originalImage);

    namedWindow("Grey", WINDOW_AUTOSIZE);
    imshow("Grey", greyImage);

    waitKey(0);
    return 0;
}