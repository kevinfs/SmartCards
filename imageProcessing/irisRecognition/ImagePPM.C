#include"ImagePPM.h"
#include<fstream>
#include<iostream>
#include<sstream>
#include<string.h>
#include<stdlib.h>
#include<string>

using namespace std;



ImagePPM::ImagePPM() {
	_type = "P6";
}

ImagePPM::ImagePPM(int x, int y)
:Image(x, y) {
	_type = "P6";
	_size = _width * _height;
}

int ImagePPM::findPixel(int x, int y) {

	return (y * _width + x);
}

int ImagePPM::arraySize() {
	return _size * 3;
}

ImagePGM ImagePPM::grayscale() {

	byte grayArray[_size];
	byte average = 0;
	int k = 0;
    /************ Pour stocker une image ppm avec un pixel représenté par 3 données (RGB) en un seul ***********/
	for (int i = 0; i <= arraySize() - 3; i += 3) {
		average = (_array[i] + _array[i + 1] + _array[i + 2]) / 3;
		grayArray[k] = average;
		k++;
	}

	ImagePGM img(_width, _height, grayArray);
	return img;

}

int *ImagePPM::histogrammeR() {

    int *histo = new int[256];

    for (int i = 0; i < 256; i++)
        histo[i] = 0;

    for (int j = 0; j < arraySize() - 3; j +=3) {

        histo[_array[j]]++;
    }

    return histo;
}

ImagePPM::histoPixel *ImagePPM::histogrammeRGB() {

    ImagePPM::histoPixel *histo = new histoPixel[256];

    for (int i = 0; i < 256; i++){
        histo[i].red = 0;
        histo[i].green = 0;
        histo[i].blue = 0;

    }

    for (int j = 0; j < arraySize() - 3; j +=3) {

        histo[_array[j]].red++;
        histo[_array[j+1]].green++;
        histo[_array[j+2]].blue++;

    }

    // for (int j = 0; j < arraySize(); j++) {

    //     histo[_array[j]]++;
    // }

    return histo;
}
