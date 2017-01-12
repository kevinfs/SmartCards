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
				// printf("theta first picture value : %f \n",theta);
				itup = rTable.lower_bound(theta)->first;   // itup is the "pas" directly above theta
				itlow = itup - 180/pas;  // itlow is the "pas" directly below theta
			    // printf("itlow :%d itup : %d",itlow,itup);
			    // std::cout << itlow->first  << " lower " << itlow->first -gDirection[i][j]  << std::endl;
			    // std::cout << itup->first   << " upper " << itup->first -gDirection[i][j]  << std::endl;
			    // std::cout << itup->first   << " test " << (itup->first -gDirection[i][j])  -  (itlow->first -gDirection[i][j]) << std::endl;
			    diffrence = itlow - theta ;
			    // printf(" la difference est :%f",diffrence);
			    if ( abs(diffrence) < 2.5)
			    {
			    	pos = rTable.find(itlow);
			    }else{
			    	pos = rTable.find(itup);
			    }
			     // std::cout << " pos first " << pos->first << std::endl;

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
		// std::cout << "theta " << it->first  << " value " << value.size() << std::endl;
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
	int xCscaled = 0, yCscaled = 0 ;
	int scaleMax = 5;
	int scaleMin = 1;
	int centerScale =0 ;
	int centerRotation = 0 ;
	int xA = 0;
	int yA = 0;

	int maxI = 0;
	int maxJ = 0 ;
  	std::vector<RBeta>  value;

	width =img->get_width();
	height = img->get_height();
	accumulation = *img;
	// std::cout << " width " << width << " height " << height << std::endl;
	int H[width][height][scaleMax];
	
	for (int i = 0; i < width; i++)
	{
		for (int j = 0; j < height; j++)
		{
			
				for (int y = scaleMin; y < scaleMax; y++)
				{
										// printf("i %d j %d x : rotate %d y : scale %d \n", i , j, x, y );
					H[i][j][y]=0;

				}/* code */
			
			// accumulation.modifyImg(i, j,0);

		}
	}

	  /* Allocation dynamique */
	gDirection = new double* [width];
	for (int i=0; i < width; i++)
		gDirection[i] = new double[height];

		*img = img->sobel(70, gDirection);


	for (int i = 0; i < width; i++)
	{
		for (int j = 0; j < height; j++)
		{

			if (gDirection[i][j]!=600.0 )
			{
				theta = gDirection[i][j] - 90.0; // theta is the direction of the tangent
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

   			//  			xA = r * cos(beta);
						// yA = r * sin(beta);
			
			   			
							for (int t = scaleMin; t < scaleMax; t++)
							{
								// xc = i - (xA * cos(s) - yA * sin(s))*t ;
								// yc = i - (xA * sin(s) - yA * cos(s))*t ;

								xc = i + r * t * cos(beta );
			 					yc = j + r * t * sin(beta );
			 					if (xc < width && yc < height && xc > 0 && yc >0)
		   			 			{
		   			 				H[xc][yc][t]++;
		   			 			}
							}/* code */
									
   			 			
				    }
				}
			}
		}
	}
	for (int l = 0; l < width; l++)
	{
		for (int m = 0; m < height; m++)
		{
							
				for (int q = scaleMin; q < scaleMax; q++)
				{
					if (H[l][m][q]> max)
					{
						max = H[l][m][q];
						maxi = l;
						maxj = m;
						centerScale = q ;
					}

					    // printf("x %d y %d valeur %d \n",l,m , H[l][m][q] );
				}
			
			
		}

	}

	for (int n = 0; n < width; n++)
	{
		for (int o = 0; o < height; o++)
		{
			
				for (int j = scaleMin; j < scaleMax; j++)
				{
				    // printf("x %d y %d valeur %d \n",n, o, H[n][o][i][j] );
					accumulation.modifyImg(n, o,H[n][o][j]*255/max);	
					/* code */
				}
			
			
		}
	}
	accumulation.saveImage("img/accumulation.pgm");// Enregistrement de l'image

	std::cout << " valeur maximale atteinte H[xc][yc] = " << H[maxi][maxj][centerScale] << " maxi " << maxi << " maxj " << maxj << std::endl; 
	for(int w =0 ; w <30 ; w++){
		maxI = maxi+w;
		maxJ = maxj+w;
		img->modifyImg(maxI, maxJ ,255);
	}
	
	*xcentre = maxi;
	*ycentre = maxj;

}

void imgReconstruction(ImagePGM * img, std::map<int, std::vector<RBeta> > rTable, int * xcentre, int * ycentre){

}

void generalizedHoughTransform(){
		std::map<int, std::vector<RBeta> > ellipseRtable, circleRtable;
		ImagePGM ellipse, ellipse2;     // déclaration de l'image
		ImagePGM circle;
		ImagePPM img;
		int xc=0, yc=0;

		// Generate Rtable in order to recognize the ellipse
		ellipse.loadImage("img/ellipse6.pgm"); // chargement de l'image
		
		// img.loadImage("img/loginPicture1.ppm");
		// ImagePGM img2 = img.grayscale();
		// img2.saveImage("img/loginPicture1Gray.pgm");
		// // ellipse2.loadImage("img/loginPictureGray.pgm"); // chargement de l'image
		ellipse2.loadImage("img/ellipse6.pgm"); // chargement de l'image
		// ellipse2.loadImage("img/loginPicture1Gray.pgm"); // chargement de l'image

		ellipseRtable = generateRtable(&ellipse);
		accumulationTable(&ellipse2, ellipseRtable, &xc , &yc);

		ellipse2.saveImage("img/sami_sobel_center.pgm");// Enregistrement de l'image


}
	
