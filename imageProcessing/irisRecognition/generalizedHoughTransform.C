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

int pas = 36;

double computeR(int xc, int yc, int x, int y){
	return sqrt(pow(x-xc, 2)+ pow(y-yc,2));
}

double computeBeta(int xc, int yc, int x, int y){
	return atan((double)(y-yc)/(double)(x-xc));
}

std::map<int, std::vector<RBeta> > generateRtable(ImagePGM *img){
	

    double ** gDirection ;
    int width=0;
	int height=0;	
	int xc=0, yc=0;
	std::map<int, std::vector<RBeta> > rTable;
	RBeta rbeta;
  	std::map<int, std::vector<RBeta> >::iterator itlow,itup;

  	std::map<int, std::vector<RBeta> >::iterator pos;
  	std::vector<RBeta>  value;
  	double theta=0.0;
	// initialisation des theta
	for ( int theta1 =0 ; theta1 <= 180; theta1 += 180/pas)
	{
		rTable.insert(std::make_pair(theta1, value));
	}

// Print the keys of the map
	  // // Iterate through all elements in std::map 
    // while(it != rTable.end())
    // {
    //     std::cout << it->first  << std::endl;
    //     it++;
    // }

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
				// printf("i = %d, j = %d gDirection %f\n", i,j, gDirection[i][j]);
				rbeta.r = computeR(xc, yc, i,j);
				rbeta.beta = computeBeta(xc, yc, i,j)*180/M_PI;

				// printf("r %f beta %f \n", rbeta.r, rbeta.beta );
				theta = gDirection[i][j] + 90.0;
				itlow=rTable.lower_bound(theta);  // lower theta to the gradient
				itup=rTable.upper_bound (theta);   // itup points to e (not d!)

			    // std::cout << itlow->first  << " lower " << itlow->first -gDirection[i][j]  << std::endl;
			    // std::cout << itup->first   << " upper " << itup->first -gDirection[i][j]  << std::endl;
			    // std::cout << itup->first   << " test " << (itup->first -gDirection[i][j])  -  (itlow->first -gDirection[i][j]) << std::endl;
			    if ((itlow->first -theta)>=0 && (itlow->first -theta)<2.4)
			    {
			    	pos = rTable.find(itlow->first);
			    }else{
			    	pos = rTable.find(itup->first);
			    }
			    // std::cout << " pos " << pos->first << std::endl;

				if (pos == rTable.end()) {
				    //handle the error
				    std::cout << "error" << std::endl;

				} else {
				    value = pos->second;
				    // printf("size %lu\n", value.size());
				    value.push_back(rbeta);
				    rTable[pos->first]= value;
				}
			}
		}		
	}
    std::map<int,  std::vector<RBeta> >::iterator it = rTable.begin();

    while(it != rTable.end())
    {
        value = it->second;
		std::cout << "theta " << it->first  << " value " << value.size() << std::endl;
        it++;

        // printf("size %lu\n", value.size());
       //   for (int i=0; i<value.size(); i++)
   			 // std::cout << i <<" " << value.at(i).r <<" " << value.at(i).beta << std::endl;

    }
    return rTable;
}
void accumulationTable(ImagePGM * img, std::map<int, std::vector<RBeta> > rTable, int * xcentre, int * ycentre){

    double ** gDirection ;
    int width=0;
	int height=0;	
	double r=0, beta=0;
	int max = 0;
	int maxi=0, maxj=0;
	double theta = 0.0;
	ImagePGM accumulation;


  	// std::map<int, std::vector<RBeta> >::iterator itlow,itup;
  	int itlow = 0;
  	int itup = 0;

  	std::map<int, std::vector<RBeta> >::iterator pos;

	int xc=0, yc=0;

  	std::vector<RBeta>  value;

	width =img->get_width();
	height = img->get_height();
	accumulation = *img;
	// std::cout << " width " << width << " height " << height << std::endl;
	int H[width][height];
	for (int i = 0; i < width; i++)
	{
		for (int j = 0; j < height; j++)
		{
			H[i][j]=0;
			// accumulation.modifyImg(i, j,0);

		}
	}

	  /* Allocation dynamique */
	gDirection = new double* [width];
	for (int i=0; i < width; i++)
		gDirection[i] = new double[height];

	*img = img->sobel(180, gDirection);
	img->saveImage("img/elsobel.pgm");// Enregistrement de l'image


	for (int i = 0; i <= width-1; i++)
	{
		for (int j = 0; j <= height-1; j++)
		{

			if (gDirection[i][j]!=0.0)
			{
				theta = gDirection[i][j]+90.0; // theta is the direction of the tangent
				itup = rTable.lower_bound(theta)->first;   // itup is the "pas" directly above theta
				itlow = itup - 180/pas;  // itlow is the "pas" directly below theta
				
				// std::cout << theta << endl;
			    // std::cout << itlow  << " lower " << itlow - gDirection[i][j]  << std::endl;
			    // std::cout << itup   << " upper " << itup - gDirection[i][j]  << std::endl;
			    // std::cout << itup   << " test " << (itup - gDirection[i][j])  -  (itlow - gDirection[i][j]) << std::endl;
			    
				// We choose between itup and itlow : which one is the closest ?
				// pos is the position inside the rTable
			    if (abs(itlow - theta)<2.4)
			    {
			    	// we are closer to itlow
			    	pos = rTable.find(itlow);
			    }else{
			    	// we are closer to itup
			    	pos = rTable.find(itup);
			    }

				if (pos == rTable.end()) {
				    //handle the error
				    std::cout << "error" << std::endl;

				} else {
					// Fine, we get beta and the radius
				    value = pos->second;
				    // printf("size %lu\n", value.size());
				    for (int i =0; i<value.size(); i++){
   			 			// std::cout << i <<" " << value.at(i).r <<" " << value.at(i).beta << std::endl;
   			 			r = value.at(i).r;
   			 			beta = value.at(i).beta;
   			 			xc= i + r * cos(beta);
   			 			yc = j +r * sin(beta);
   			 			if (xc < width && yc < height && xc >0 && yc >0)
   			 			{
   			 				H[xc][yc]++;

						// std::cout << " i " << i <<" j " << j <<" xc " << xc <<" yc " << yc << std::endl;
   			 			// std::cout << " H[xc][yc] " << H[xc][yc] << std::endl; 
   			 			}
				    }
				}
			}
		}
	}
	for (int i = 0; i < width; i++)
	{
		for (int j = 0; j < height; j++)
		{
			if (H[i][j]> max)
			{
				max = H[i][j];
				maxi = i;
				maxj = j;
			}
		}
	}

	for (int i = 0; i < width; i++)
	{
		for (int j = 0; j < height; j++)
		{
			accumulation.modifyImg(i, j,H[i][j]*255/max);
		}
	}
	accumulation.saveImage("img/accumulation.pgm");// Enregistrement de l'image

	std::cout << " H[xc][yc] " << H[maxi][maxj] << " maxi " << maxi << " maxj " << maxj << std::endl; 

	img->modifyImg(maxi, maxj,255);
	*xcentre = maxi;
	*ycentre = maxj;


}

void imgReconstruction(ImagePGM * img, std::map<int, std::vector<RBeta> > rTable, int * xcentre, int * ycentre){

}
void generalizedHoughTransform(){
		std::map<int, std::vector<RBeta> > ellipseRtable, circleRtable;
		ImagePGM ellipse, ellipse2;     // déclaration de l'image
		ImagePGM circle;
		int xc=0, yc=0;

		// Generate Rtable in order to recognize the ellipse
		ellipse.loadImage("img/ellipse1.pgm"); // chargement de l'image
		ellipse2.loadImage("img/ellipse1.pgm"); // chargement de l'image

		ellipseRtable = generateRtable(&ellipse);
		accumulationTable(&ellipse2, ellipseRtable, &xc , &yc);

		ellipse2.saveImage("img/ellipse_sobel.pgm");// Enregistrement de l'image

		// Generate Rtable in order to recognize the circle

		// circle.loadImage("img/circle1.pgm"); // chargement de l'image
		// circleRtable = generateRtable(&circle);
		// ellipse2.loadImage("img/circle1.pgm"); // chargement de l'image

		// accumulationTable(&ellipse2, circleRtable);
		// circle.saveImage("img/circle_sobel.pgm");// Enregistrement de l'image

		// ellipse.loadImage("img/cerise.pgm"); // chargement de l'image
		// generateRtable(&ellipse);
		// ellipse.saveImage("img/cerise_sobel.pgm");// Enregistrement de l'image

}
	
