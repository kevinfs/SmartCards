// cd Documents/cours/M2/atelier/TI/
//  main.c
//  
//
//  Created by Gada Rezgui on 03/10/2016.
//
//

//
//  main.c
//

#include <opencv/cv.h>
#include <opencv/highgui.h>

#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <dirent.h>
#include <sys/stat.h>



int main( void ) {
    IplImage * iris = 0; 
    const char* filename = "img/irisBlue.jpg";
    
    // //  /**************** USE FILE (video) ************/
    if (!(iris=cvLoadImage(filename, CV_LOAD_IMAGE_COLOR ))){//capture from file
        printf("Cannot initialize file\n");
    }
    // /************************ Realease memory **************************/
    
    cvShowImage("iris", iris);

    cvReleaseImage(&iris);
    /******************************************************************/
    
    return 0;
}
