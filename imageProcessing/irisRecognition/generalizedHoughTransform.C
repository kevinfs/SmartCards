#include <iostream>
#include <unistd.h>
#include <stdio.h>
#include <string.h>
#include <fstream>

#include "generalizedHoughTransform.h"

// tuto suivi pr hought transform generalized : http://fourier.eng.hmc.edu/e161/lectures/hough/node6.html

using namespace std;
// voir std::map pour hashmap et std::vector pr arraylist
// diviser le main

void generateRtable(ImagePGM *img){
	
    double ** gDirection ;
    int width=0;
	int height=0;	int xc=0, yc=0;

//  Width et height a verifier peut etre l'inverse
	width =img->get_width();
	height = img->get_height();

	  /* Allocation dynamique */
	gDirection = new double* [width];
	for (int i=0; i < width; i++)
		gDirection[i] = new double[height];

	img->barycentre(&xc,&yc);
	printf("xc = %d yc = %d\n",xc, yc );
	*img = img->sobel(180, gDirection);
	for (int i = 0; i < width; i++)
	{
		for (int j = 0; j < height; j++)
		{
		/* code */
			printf("i = %d, j = %d gDirection %f\n", i,j, gDirection[i][j]);
		}		
	}

	


}