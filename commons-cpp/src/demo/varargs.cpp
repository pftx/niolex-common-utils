#include <iostream>
#include <initializer_list>

const int Size = 5;

template<typename T>
T average_list(std::initializer_list<T> a)
{
	T t = 0;
	int cnt = 0;
	for (auto p = a.begin(); p != a.end(); ++p, ++cnt)
	{
		t += *p;
	}
	
	return t / cnt;
}

void sum_val(long double &r){std::cout << "Don't do this!";}

template<typename T>
void sum_val(long double &r, const T &t){ r += t; }

template<typename T, typename ... Args>
void sum_val(long double &r, const T &t, Args ... args){ r += t; sum_val(r, args...); }


template<typename ... Args>
long double sum_values(Args ... args)
{
	long double r = 0;
	sum_val(r, args...);
	return r;
}

int main()
{
	using namespace std;
	// list of double deduced from list contents
	auto q = average_list({15.4, 10.7, 9.0});
	cout << q << endl;
	// list of int deduced from list contents
	cout << average_list({20, 30, 19, 17, 45, 38} ) << endl;
	
	// forced list of double
	auto ad = average_list<double>({'A', 70, 65.33});
	cout << ad << endl << endl;
	
	cout << sum_values() << endl;
	cout << sum_values(40) << endl;
	cout << sum_values(30, 40) << endl;
	cout << sum_values(20, 30, 40) << endl;
	cout << sum_values(10, 20, 30, 40) << endl;
	cout << sum_values('a', 10, 20, 30, 40) << endl;
	cout << sum_values(0.01, 'a', 10, 20, 30, 40) << endl;
	return 0;
}