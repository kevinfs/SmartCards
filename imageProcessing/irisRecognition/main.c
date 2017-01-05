#include<iostream>
#include<unistd.h>
#include<fcntl.h>
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include "ImagePPM.h"
#include "ImagePGM.h"


using namespace std;

int main(int argc, char* argv[]) {
    int c;
    extern char *optarg;
    extern int optind;
    char* filename;
    
    char* threshold;
//    char* strthreshold='120';
    if (argc==3) {
        filename = argv[argc - 1];

        threshold="120";
    }else if(argc == 1){
        cerr<<"No file found"<<endl;
        cerr << "Usage:\n" << argv[0] << " [-s] file threshold\n or \nUsage:\n" << argv[0] << " [-s] file"<<endl;
        exit(0);

    }
    else{
         filename = argv[argc - 2];
        threshold = argv[argc - 1];
    }
    char* token, *tmp;
    string name(filename);

    while ((c = getopt(argc, argv, "s")) != EOF)
        switch (c) {
        case 's': // sobel
            token = strtok(filename, ".");
            while (token) {
                tmp = token;
                token = strtok(NULL, ".");
            }
            if (strcmp(tmp, "ppm") == 0) {
                try {
                    ImagePPM img;
                    img.load(name.c_str());
                    ImagePGM img2 = img.convertToGrayscale();
                    ImagePGM img3 = img2.gradian();
                    ImagePGM img4 = img3.sobel(atoi(threshold));
                    strcat(filename, "_sobel.pgm");
                    img4.save(filename);
                } catch (InvalidImageType e) {
                    cerr << "type is " << e._actual_type
                            << ", excepted type is " << e._excepted_type
                            << endl;
                }
            }
            
            else if (strcmp(tmp, "pgm") == 0) {
                try {
                    ImagePGM img;
                    img.load(name.c_str());
            img = img.gradian();
                    img = img.sobel(atoi(threshold));
                    strcat(filename, "_sobel.pgm");
                    img.save(filename);
                } catch (InvalidImageType e) {
                    cerr << "type is " << e._actual_type
                    << ", excepted type is " << e._excepted_type
                    << endl;
                }
            }
            else
                cout<< " type is not recognized "<<endl;
            break;
        case 'h': // help
            cout << "Usage:\n" << argv[0] << " [-s] file\n";
            cout << "   -s : apply sobel filter\n";
            return 0;
        default:
            cerr << "invalid command\n";
            return 1;
        }
    return 0;
}
