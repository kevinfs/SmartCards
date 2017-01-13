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
	// printf("xc = %d yc = %d\n",xc, yc );
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
				    // std::cout << "error" << std::endl;

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
	int maxI = 0,maxJ = 0;


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

	*img = img->sobel(140, gDirection);


	for (int i = 0; i < width; i++)
	{
		for (int j = 0; j < height; j++)
		{

			if (gDirection[i][j]!=600.0 )
			{
				theta = gDirection[i][j] - 90.0; // theta is the direction of the tangent
				// printf(" second picture theta values are :%f \n",theta);
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
				    // std::cout << "error" << std::endl;

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
			// printf("x %d y %d valeur %d \n",n, o, H[n][o] );
			accumulation.modifyImg(n, o,H[n][o]*255/max);
		}
	}
	accumulation.saveImage("accumulation.pgm");// Enregistrement de l'image

	std::cout << " valeur maximale atteinte H[xc][yc] = " << H[maxi][maxj] << " maxi " << maxi << " maxj " << maxj << std::endl; 

	img->modifyImg(maxi, maxj,255);
	*xcentre = maxi;
	*ycentre = maxj;

	for(int w =0 ; w <30 ; w++){
		maxI = maxi+w;
		maxJ = maxj+w;
		img->modifyImg(maxI, maxJ ,255);
	}


}

ImagePPM imgReconstruction( ImagePPM img, std::map<int, std::vector<RBeta> > rTable, int  xcentre, int  ycentre){
	// printf("xc %d yc %d \n", xcentre, ycentre );
	ImagePGM visage = img.grayscale();
    std::map<int,  std::vector<RBeta> >::iterator it = rTable.begin();
    int i=0,j=0;
	double r=0, beta=0;
	std::vector<RBeta>  value;

    int width=0;
	int height=0;
	int * accumulatedR;
	int max = 0, maxR=0;
	int taille = 0;

	double VTheta=0.0;

	width =visage.get_width();
	height = visage.get_height();
	for (int i = 0; i < width; i++)
	{
		for (int j = 0; j < height; j++)
		{
	 		visage.modifyImg(i, j ,0);
			// accumulation.modifyImg(i, j,0);

		}
	}
	if (width>height)
	{
		 accumulatedR = new int [width];
		for (int i = 0; i < width; i++)
		{
			accumulatedR[i]=0;
		}
		taille = width;
	}else{
		 accumulatedR = new int [height];
		for (int i = 0; i < height; i++)
		{
			accumulatedR[i]=0;
		}
		taille = height;
	}


	// Print the keys of the map
	  // Iterate through all elements in std::map 
    while(it != rTable.end() && it->first<0)
    {
        // std::cout << it->first  << std::endl;

        value = it->second;
	    // // printf("size %lu\n", value.size());
	    for (int k =0; k<value.size(); k++){
	 			r = value.at(k).r;
	 			accumulatedR[(int)r] ++;
	 			beta = value.at(k).beta;
	 			// std::cout << k <<" " << value.at(k).r <<" " << value.at(k).beta << " value size "<< value.size()<< std::endl;

	 			i = xcentre - r * cos(beta);
	 			j = ycentre - r * sin(beta);
	 			// printf("i = %d j %d \n",i,j );
	 			// visage.modifyImg(xcentre, ycentre ,255);

	 			if (i < width && j < height && i >0 && j >0)
   			 	{
	 				// visage.modifyImg(i, j ,255);
	 				// if (i < width-3 && j < height-3 && i >3 && j >3)
   			//  		{
		 			// 	visage.modifyImg(i-1, j ,255);
		 			// 	visage.modifyImg(i, j-1 ,255);
		 			// 	visage.modifyImg(i+1, j ,255);
		 			// 	visage.modifyImg(i, j+1 ,255);

		 			// 	visage.modifyImg(i-1, j-1 ,255);
		 			// 	visage.modifyImg(i+1, j+1 ,255);

		 			// 	visage.modifyImg(i-2, j ,255);
		 			// 	visage.modifyImg(i, j-2 ,255);
		 			// 	visage.modifyImg(i+2, j ,255);
		 			// 	visage.modifyImg(i, j+2 ,255);

		 			// 	visage.modifyImg(i-2, j-2 ,255);
		 			// 	visage.modifyImg(i+2, j+2 ,255);

		 			// 	visage.modifyImg(i-3, j ,255);
		 			// 	visage.modifyImg(i, j-3 ,255);
		 			// 	visage.modifyImg(i+3, j ,255);
		 			// 	visage.modifyImg(i, j+3 ,255);

		 			// 	visage.modifyImg(i-3, j-3 ,255);
		 			// 	visage.modifyImg(i+3, j+3 ,255);

	 				// }

   				}


			// std::cout << " i " << i <<" j " << j <<" xc " << xc <<" yc " << yc << std::endl;
	 			// std::cout << " H[xc][yc] " << H[xc][yc] << std::endl; 
	    }

        it++;

	}
		for (int m = 0; m < taille; m++)
		{
			if (accumulatedR[m]> max)
			{
				max = accumulatedR[m];
				maxR = m;
			}
		}
		// printf("max %d r%d\n",max, maxR );
		for (int radii = 0; radii < maxR; radii++)
		{
			// for (int VTheta = 0; VTheta < 360; VTheta++)
			// {
			VTheta=0;
				while (VTheta <360.0){

	 			i = xcentre + radii * cos(VTheta);
	 			j = ycentre + radii * sin(VTheta);

	 			if (i < width && j < height && i >0 && j >0)
   			 	{
   			 			 			// printf("i = %d j %d VTheta %f\n",i,j, VTheta );

	 				visage.modifyImg(i, j ,255);
	 			}
	 			VTheta += 0.01; 
			}
			/* code */
		}


	 			// if (i < width && j < height && i >0 && j >0)
   		// 	 	{
	 			// 	visage.modifyImg(i, j ,255);
	 			// }

	// visage.dilatation();
	// visage.dilatation();
	// visage.dilatation();
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
			// printf("i = %d j %d value %d \n",i,j , visage.getPixel(i,j));

				if (visage.getPixel(i,j)==0)
				{
					// printf("i = %d j %d \n",i,j );
					img.modifyImg(i*3,j*3,0,0,0);
				}

			}	
		}

	// visage.saveImage("img/visage.pgm");// Enregistrement de l'image
	// img.saveImage("/visageColor.ppm");// Enregistrement de l'image
	return img;

}

ImagePPM generalizedHoughTransform(string pictureName){
		std::map<int, std::vector<RBeta> > ellipseRtable, circleRtable;
		ImagePGM ellipse, ellipse2, img2;     // déclaration de l'image
		ImagePGM circle;
		ImagePPM img, newImg;
		int xc=0, yc=0;

		// Generate Rtable in order to recognize the ellipse
		ellipse.loadImage("ellipse5.pgm"); // chargement de l'image

// "img/loginPicture.ppm"
		img.loadImage(pictureName);
		img2 = img.grayscale();
		// img2.saveImage("img/loginPictureGray.pgm");
		// ellipse2.loadImage("img/loginPictureGray.pgm"); // chargement de l'image
		
		// ellipse2.loadImage("img/visage.pgm"); // chargement de l'image

		ellipseRtable = generateRtable(&ellipse);
		accumulationTable(&img2, ellipseRtable, &xc , &yc);

		newImg = imgReconstruction(img, ellipseRtable, xc, yc );
		// circle.loadImage("img/circle1.pgm"); // chargement de l'image
		// circleRtable = generateRtable(&circle);


		// // img2 = newImg.grayscale();
		// // img2.saveImage("img/visageGray.pgm");// Enregistrement de l'image
		// img2.loadImage("img/visageGray.pgm");
		// accumulationTable(&img2, circleRtable, &xc , &yc);

		// img2.saveImage("img/samsam110_sobel_center.pgm");// Enregistrement de l'image

		return newImg;

}