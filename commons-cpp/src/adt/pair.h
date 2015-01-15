#ifndef _LEX_ADT_PAIR_H_
#define _LEX_ADT_PAIR_H_

namespace adt
{

template<typename A, typename B>
class Pair
{
private:
	A _a;
	B _b;

public:
	Pair()
	{
	}

	Pair(const A &a, const B &b) :
			_a(a), _b(b)
	{
	}

	A &a()
	{
		return _a;
	}

	B &b()
	{
		return _b;
	}

	const A &a() const
	{
		return _a;
	}

	const B &b() const
	{
		return _b;
	}
};

}

#endif
