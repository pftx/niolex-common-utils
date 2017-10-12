// random.cpp -- random access to a binary file
#include <iostream> // not required by most systems
#include <fstream>
#include <iomanip>
#include <cstdlib> // for exit()

using namespace std;

const int LIM = 20;

struct planet
{
char name[LIM]; // name of planet
double population; // its population
double g; // its acceleration of gravity
};

const char * file = "planets.dat"; // ASSUMED TO EXIST (binary.cpp example)

inline void eatline() { while (std::cin.get() != '\n') continue; }

inline void showRec(const planet &pl, int ct)
{
  cout << ct << ": " << setw(LIM) << pl.name << ": " << setprecision(0) << setw(12) << pl.population
    << setprecision(2) << setw(6) << pl.g << endl;
}

inline void readRec(planet &pl)
{
  cout << "Enter planet name: ";
  cin.get(pl.name, LIM);
  if (!cin)
  {
    cerr << "You entered blank name, Bye!\n";
	exit(EXIT_FAILURE);
  }
  eatline();

  cout << "Enter planetary population: ";
  cin >> pl.population;

  cout << "Enter planet's acceleration of gravity: ";
  cin >> pl.g;
}

inline int show(fstream &finout)
{
  int ct = 0;
  planet pl;
  
  finout.seekg(0); // go to beginning
  cout << "Here are the current contents of the " << file << " file:\n";
  while (finout.read((char *) &pl, sizeof pl))
  {
    showRec(pl, ct);
	++ct;
  }
  
  if (finout.eof())
    finout.clear(); // clear eof flag
  else
  {
    cerr << "Error in reading " << file << ".\n";
    exit(EXIT_FAILURE);
  }

  return ct;
}

int main()
{
cout << fixed;

planet pl;
fstream finout; // read and write streams
finout.open(file, ios_base::in | ios_base::out | ios_base::binary);
//NOTE: Some Unix systems require omitting | ios::binary
if (!finout.is_open())
{
  cerr << file << " could not be opened -- Bye.\n";
  exit(EXIT_FAILURE);
}

int ct = 0;
// show initial contents
ct = show(finout);

while (true)
{
  // change a record
  cout << "Enter the record number you wish to change: ";
  long rec;
  cin >> rec;
  eatline(); // get rid of newline
  if (rec < 0 || rec > ct)
  {
    cerr << "Invalid record number -- Bye.\n";
	break;
  }

  streampos place = rec * sizeof pl; // convert to streampos type
  
  if (rec != ct)
  {
    finout.seekg(place); // random access
    if (finout.fail())
    {
      cerr << "Error on attempted seek\n";
      exit(EXIT_FAILURE);
    }

    finout.read((char *) &pl, sizeof pl);
    if (finout.eof())
      finout.clear(); // clear eof flag
  
    cout << "Your selection:\n";
    showRec(pl, rec);
  }
  
  readRec(pl);

  finout.seekp(place); // go back
  finout.write((char *) &pl, sizeof pl) << flush;
  
  if (finout.fail())
  {
    cerr << "Error on attempted write\n";
    exit(EXIT_FAILURE);
  }
  
  // show revised file
  ct = show(finout);
}

finout.close();
cout << "Done.\n";
return 0;
}