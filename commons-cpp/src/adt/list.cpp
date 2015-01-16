#include "list.h"

namespace adt
{

template <> ostream & operator <<(ostream &os, const List<int> &li)
{
	for (int i = 0; i < li.top; i += 5)
	{
		os << li[i];
		for (int j = i + 1, k = 0; j < li.top && k < 4; ++j, ++k)
		{
			os << ", " << li[j];
		}
		os << std::endl;
	}
	return os;
}

template <> ostream & operator <<(ostream &os, const List<String> &li)
{
	for (int i = 0; i < li.top; ++i)
	{
		os << i << "\t" << li[i] << "\n";
	}
	return os;
}

}
