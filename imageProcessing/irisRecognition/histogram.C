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
int main() {
	char inputImage[50];  // récupération du nom de l'image saisi par l'utilisateur
    char* image; 
    ofstream myfile;

    // cout << "Color histogram C++ program" <<endl;

    // cout << "Entrer le nom de votre fichier image : ";
    // cin >> inputImage;
    inputImage = "img/ellipse1.ppm"
    image = inputImage ;
   
	char* charPointer, *extension;
    string name(image);

	// séparation de l'extension du nom de l'image en se basant sur le delimiteur "."
	charPointer = strtok(image, ".");
	
	while (charPointer) {
		extension = charPointer; // récupération de l'extension
		charPointer = strtok(NULL, "."); 
	} 
	  /********************************************************************************/
	 /********************************* Image PPM ************************************/
	/********************************************************************************/
	if (strcmp(extension, "ppm") == 0) { // condition sur le type de fichier ppm ou pgm
		
		ImagePPM img;     // déclaration de l'image
		ImagePGM img2;
		img.loadImage(name.c_str()); // chargement de l'image
		myfile.open ("histo/histoGrammeRGB.txt");
		img2 = img.grayscale();
		generateRtable(&img2);

		strcat(image, "_sobel.pgm");// Nouveau nom du fichier
        img2.saveImage(image);// Enregistrement de l'image


		ImagePPM::histoPixel *pInt = img.histogrammeRGB();
        
        for (int i = 0; i < 256; ++i) {
            
            // printf("pour la couleur :%d le rouge: %d le vert: %d le bleu :%d \n", i, pInt[i].red, pInt[i].green, pInt[i].blue );
        	
        	myfile << pInt[i].red
        		   << " "
        		   << pInt[i].green
        		   << " "
        		   << pInt[i].blue
        		   << "\n";
        }
		
		myfile.close();			
	}
	
				
return 0;

}
