#include <iostream>
#include <unistd.h>
#include <stdio.h>
#include <string.h>
#include <fstream>

#include "ImagePPM.h"

using namespace std;

int main(int argc, char* argv[]) {

	char inputImage[50];  // récupération du nom de l'image saisi par l'utilisateur
    char* image; 
    ofstream myfile;
    char* histogrameFile;

    image = argv[1]; 

    histogrameFile = strcat(argv[2],".txt");
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
		myfile.open(histogrameFile);
		img2 = img.grayscale();
		img2 = img2.sobel(110);
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
