#include "list.h"

namespace adt
{

template <> ostream & operator <<(ostream &os, const List<int> &li)
{
#define LINE_SIZE 7
#define FIELD_WIDTH 10
	for (int i = 0; i < li.top; ++i)
	{
		os.width(FIELD_WIDTH);
		os << li[i];
		if (i % LINE_SIZE == LINE_SIZE - 1)
		{
			os << std::endl;
		}
		else
		{
			os << ",";
		}
	}

	if (li.top % LINE_SIZE != 0)
		os << std::endl;
#undef LINE_SIZE
#undef FIELD_WIDTH
	return os;
}

template <> ostream & operator <<(ostream &os, const List<long> &li)
{
#define LINE_SIZE 4
#define FIELD_WIDTH 18
	for (int i = 0; i < li.top; ++i)
	{
		os.width(FIELD_WIDTH);
		os << li[i];
		if (i % LINE_SIZE == LINE_SIZE - 1)
		{
			os << std::endl;
		}
		else
		{
			os << ",";
		}
	}

	if (li.top % LINE_SIZE != 0)
		os << std::endl;
#undef LINE_SIZE
#undef FIELD_WIDTH
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
