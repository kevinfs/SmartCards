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
  	// std::map<int, std::vector<RBeta> >::iterator itlow,itup;
  	int itlow = 0;
  	int itup = 0;
  	double diffrence = 0;
  	std::map<int, std::vector<RBeta> >::iterator pos;
  	std::vector<RBeta>  value;
  	double theta=0.0;
	// initialisation des theta
	for ( int theta1 = -90 ; theta1 <= 90; theta1 += 180/pas)
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
			if (gDirection[i][j]!=600.0)
			{
				// printf("i = %d, j = %d gDirection %f\n", i,j, gDirection[i][j]);
				rbeta.r = computeR(xc, yc, i,j);
				rbeta.beta = computeBeta(xc, yc, i,j)*180/M_PI;

				// printf("r %f beta %f \n", rbeta.r, rbeta.beta );
				theta = gDirection[i][j] - 90.0;
				printf("theta first picture value : %f \n",theta);
				itup = rTable.lower_bound(theta)->first;   // itup is the "pas" directly above theta
				itlow = itup - 180/pas;  // itlow is the "pas" directly below theta
			    printf("itlow :%d itup : %d",itlow,itup);
			    // std::cout << itlow->first  << " lower " << itlow->first -gDirection[i][j]  << std::endl;
			    // std::cout << itup->first   << " upper " << itup->first -gDirection[i][j]  << std::endl;
			    // std::cout << itup->first   << " test " << (itup->first -gDirection[i][j])  -  (itlow->first -gDirection[i][j]) << std::endl;
			    diffrence = itlow - theta ;
			    printf(" la difference est :%f",diffrence);
			    if ( abs(diffrence) < 2.5)
			    {
			    	pos = rTable.find(itlow);
			    }else{
			    	pos = rTable.find(itup);
			    }
			     std::cout << " pos first " << pos->first << std::endl;

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


	for (int i = 0; i < width; i++)
	{
		for (int j = 0; j < height; j++)
		{

			if (gDirection[i][j]!=600.0 )
			{
				theta = gDirection[i][j] - 90.0; // theta is the direction of the tangent
				printf(" second picture theta values are :%f \n",theta);
				itup = rTable.lower_bound(theta)->first;   // itup is the "pas" directly above theta
				itlow = itup - 180/pas;  // itlow is the "pas" directly below theta
				
		
				// We choose between itup and itlow : which one is the closest ?
				// pos is the position inside the rTable
			    if (abs(itlow - theta)<2.5)
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
				    for (int k =0; k<value.size(); k++){
   			 			// std::cout << i <<" " << value.at(i).r <<" " << value.at(i).beta << std::endl;
   			 			r = value.at(k).r;
   			 			beta = value.at(k).beta;
   			 			xc = i + r * cos(beta);
   			 			yc = j + r * sin(beta);
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
	for (int l = 0; l < width; l++)
	{
		for (int m = 0; m < height; m++)
		{
			if (H[l][m]> max)
			{
				max = H[l][m];
				maxi = l;
				maxj = m;
			}
		}
	}

	for (int n = 0; n < width; n++)
	{
		for (int o = 0; o < height; o++)
		{
			printf("x %d y %d valeur %d \n",n, o, H[n][o] );
			accumulation.modifyImg(n, o,H[n][o]*255/max);
		}
	}
	accumulation.saveImage("img/accumulation.pgm");// Enregistrement de l'image

	std::cout << " valeur maximale atteinte H[xc][yc] = " << H[maxi][maxj] << " maxi " << maxi << " maxj " << maxj << std::endl; 

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
		ellipse2.loadImage("img/ellipse6.pgm"); // chargement de l'image

		ellipseRtable = generateRtable(&ellipse);
		accumulationTable(&ellipse2, ellipseRtable, &xc , &yc);

		ellipse2.saveImage("img/samsam110_sobel_center.pgm");// Enregistrement de l'image

		// Generate Rtable in order to recognize the circle

		// circle.loadImage("img/circle1.pgm"); // chargement de l'image
		// circleRtable = generateRtable(&circle);
		// ellipse2.loadImage("img/circle1.pgm"); // chargement de l'image

		// accumulationTable(&ellipse2, circleRtable);
		// circle.saveImage("img/circle_sobel.pgm");// Enregistrement de l'image

		// ellipse.loadImage("img/cerise.pgm"); // chargement de l'image
		// generateRtable(&ellipse);
		// ellipse.saveImage("img/cerise_sobel.pgm");// Enregistrement de l'image



		// Generate Rtable in order to recognize the ellipse
		// ellipse.loadImage("img/ellipse1.pgm"); // chargement de l'image
		// ellipse2.loadImage("img/samsam10.pgm"); // chargement de l'image

		// ellipseRtable = generateRtable(&ellipse);
		// accumulationTable(&ellipse2, ellipseRtable, &xc , &yc);

		// ellipse2.saveImage("img/samsam10_sobel.pgm");// Enregistrement de l'image

}
	
