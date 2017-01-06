#include <iostream>
#include <unistd.h>
#include <stdio.h>
#include <string.h>
#include <fstream>

#include "ImagePPM.h"

// tuto suivi pr hought transform generalized : http://fourier.eng.hmc.edu/e161/lectures/hough/node6.html

using namespace std;


struct RBeta {
	double r;
  	double beta;
} ;


void generateRtable(ImagePGM *img);
void generalizedHoughTransform();