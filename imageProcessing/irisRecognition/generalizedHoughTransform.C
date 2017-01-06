#include <iostream>
#include <unistd.h>
#include <stdio.h>
#include <string.h>
#include <fstream>
#include <map>
#include <vector>

#include "generalizedHoughTransform.h"

// tuto suivi pr hought transform generalized : http://fourier.eng.hmc.edu/e161/lectures/hough/node6.html

using namespace std;
// voir std::map pour hashmap et std::vector pr arraylist
// diviser le main

double computeR(int xc, int yc, int x, int y){
	return sqrt(pow(x-xc, 2)+ pow(y-yc,2));
}

double computeBeta(int xc, int yc, int x, int y){
	return atan((double)(y-yc)/(double)(x-xc));
}

void generateRtable(ImagePGM *img){
	

    double ** gDirection ;
    int width=0;
	int height=0;	
	int xc=0, yc=0;
	std::map<int, std::vector<RBeta> > rTable;
	int pas = 10;
	RBeta rbeta;
  	std::map<int, std::vector<RBeta> >::iterator itlow,itup;

  	std::map<int, std::vector<RBeta> >::iterator pos;
  	std::vector<RBeta>  value;

	// initialisation des theta
	for (int theta = -90; theta <= 90; theta+= 180/pas)
	{
		rTable.insert(std::make_pair(theta, value));
	}

// Print the keys of the map
	  // // Iterate through all elements in std::map 
    std::map<int,  std::vector<RBeta> >::iterator it = rTable.begin();
    while(it != rTable.end())
    {
        std::cout << it->first  << std::endl;
        it++;
    }

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
			if (gDirection[i][j]!=0.0)
			{
				printf("i = %d, j = %d gDirection %f\n", i,j, gDirection[i][j]);
				rbeta.r = computeR(xc, yc, i,j);
				rbeta.beta = computeBeta(xc, yc, i,j);

				printf("r %f beta %f \n", rbeta.r, rbeta.beta );

				itlow=rTable.lower_bound(gDirection[i][j]);  // lower theta to the gradient
			    std::cout << itlow->first  << std::endl;
		      	pos = rTable.find(itlow->first);
				if (pos == rTable.end()) {
				    //handle the error
				    std::cout << "error" << std::endl;

				} else {
				    value = pos->second;
				    value.push_back(rbeta);
				}
			}
		}		
	}



}
void generalizedHoughTransform(){
	
		ImagePGM ellipse;     // déclaration de l'image
		ImagePGM circle;
		// Generate Rtable in order to recognize the ellipse
		ellipse.loadImage("img/ellipse1.pgm"); // chargement de l'image
		generateRtable(&ellipse);
		ellipse.saveImage("img/ellipse_sobel.pgm");// Enregistrement de l'image

		// Generate Rtable in order to recognize the circle

		circle.loadImage("img/circle1.pgm"); // chargement de l'image
		generateRtable(&circle);
		circle.saveImage("img/circle_sobel.pgm");// Enregistrement de l'image
}
	
