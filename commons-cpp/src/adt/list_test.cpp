#include "list.h"
using namespace adt;
using namespace std;

template<typename T>
ostream& Print(const T &v)
{
  return cout << v << " ";
}

int main()
{
  List<int> li{3,4,5,7,8,10};
  for(int i = 0; i < li.size(); ++i) Print<int>(li[i]);
  cout << endl;
  
  li.sort(greater<int>());
  for(int i = 0; i < li.size(); ++i) Print<int>(li[i]);
  cout << endl;
  return 0;
}
