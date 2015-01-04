#ifndef _LEX_ADT_LIST_H_
#define _LEX_ADT_LIST_H_

#include <cstdlib>

#include <iostream>
#include <stdexcept>

#include "string.h"

namespace adt
{

using std::ostream;
using std::out_of_range;

//Define template friends first.
template<typename Type> class List;
template<typename T> ostream & operator <<(ostream &os, const List<T> &li);

template<typename Type>
class List
{
private:
	Type *head;
	size_t top;
	size_t capacity;

protected:
	void assign(const List& l)
	{
		top = 0;
		while (top < l.top)
		{
			head[top] = l[top];
			++top;
		}
	}

public:
	explicit List(size_t size = 50) :
			top(0), capacity(size)
	{
		head = new Type[capacity];
	}

	List(const List &l) :
			capacity(l.capacity)
	{
		head = new Type[capacity];
		assign(l);
	}

	List & operator =(const List &l)
	{
		if (this == &l)
			return *this;

		if (capacity < l.top)
		{
			delete[] head;
			capacity = l.capacity;
			head = new Type[capacity];
		}

		assign(l);
		return *this;
	}

	~List()
	{
		delete[] head;
		capacity = -1;
	}

	size_t size() const
	{
		return top;
	}

	bool is_empty() const
	{
		return top == 0;
	}

	bool is_full() const
	{
		return top == capacity;
	}

	void sort(int (*comp)(const void *p, const void *q))
	{
		std::qsort(head, top, sizeof(Type), comp);
	}

	Type & operator [](int idx) throw (out_of_range);
	const Type & operator [](int idx) const throw (out_of_range);

	bool push_back(const Type &t);
	bool pop_back(Type &t);
	bool pop_back(Type * pt);

	void expand(int newCapacity);
	void initAll(const Type &t);

	friend ostream & operator<< <>(ostream &os, const List<Type> &li);

};

template<typename Type> Type & List<Type>::operator [](int idx) throw (out_of_range)
{
	if (idx < 0 || idx >= capacity)
		throw out_of_range("adt::list::operator[]");
	return *(head + idx);
}

template<typename Type> const Type & List<Type>::operator [](int idx) const throw (out_of_range)
{
	if (idx < 0 || idx >= capacity)
		throw out_of_range("adt::list::operator[] const");
	return *(head + idx);
}

template<typename Type> bool List<Type>::push_back(const Type &t)
{
	if (is_full())
		return false;

	*(head + top++) = t;
	return true;
}

template<typename Type> bool List<Type>::pop_back(Type &t)
{
	if (is_empty())
		return false;

	t = *(head + --top);
	return true;
}

template<typename Type> bool List<Type>::pop_back(Type * pt)
{
	if (is_empty())
		return false;

	pt = (head + --top);
	return true;
}

template<typename Type> void List<Type>::expand(int newCapacity)
{
	if (newCapacity <= capacity)
		return;

	Type *tmp = new Type[newCapacity];
	for (int i = 0; i < top; ++i)
	{
		tmp[i] = head[i];
	}

	delete[] head;
	head = tmp;
	capacity = newCapacity;
}

template<typename Type> void List<Type>::initAll(const Type &t)
{
	while (top < capacity)
	{
		head[top++] = t;
	}
}

template<typename T> ostream & operator <<(ostream &os, const List<T> &li)
{
	os << "Operation undefined.\n";
	return os;
}

template <> ostream & operator <<(ostream &os, const List<int> &li);

template <> ostream & operator <<(ostream &os, const List<String> &li);

}
#endif
