#ifndef _LEX_ADT_STRING_H_
#define _LEX_ADT_STRING_H_

#include <cstdio>
#include <cstring>

#include <iostream>
#include <stdexcept>

namespace adt
{

using std::strlen;
using std::strcpy;
using std::strcmp;

using std::cout;
using std::endl;
using std::ostream;
using std::istream;

using std::out_of_range;
using std::invalid_argument;

class String
{
private:
	char *buf;
	int len;
	int capacity;

protected:
	int nextCapSize(int str_len)
	{
		// We need one more space for NULL terminator.
		++str_len;
		int r = 16;
		while (r < str_len)
		{
			r <<= 1;
		}

		return r;
	}

	/**
	 * We will set both buf and capacity here. Only leave len for you.
	 */
	void makeBuf(const int str_len)
	{
		capacity = nextCapSize(str_len);
		buf = new char[capacity];
	}

	void ensureBufSize(const int str_len)
	{
		if (capacity > str_len)
			return;

		char *p = buf;
		makeBuf(str_len);

		strcpy(buf, p);
		delete[] p;
	}

public:
	String()
	{
		len = 0;
		makeBuf(len);

		buf[0] = '\0';
	}

	String(const char *p) throw (invalid_argument)
	{
		if (p == NULL)
			throw invalid_argument(
					"adt::string::ctor the argument char pointer is NULL");

		len = strlen(p);
		makeBuf(len);

		strcpy(buf, p);
	}

	String(const char ch)
	{
		len = 0;
		makeBuf(1);

		buf[len++] = ch;
		buf[len] = '\0';
	}

	String(const int i)
	{
		makeBuf(30);
		len = sprintf(buf, "%d", i);
	}

	String(const long &l)
	{
		makeBuf(30);
		len = sprintf(buf, "%ld", l);
	}

	String(const double &d)
	{
		makeBuf(30);
		len = sprintf(buf, "%G", d);
	}

	String(const String &o)
	{
		len = o.len;
		makeBuf(len);

		strcpy(buf, o.buf);
	}

#if __cplusplus >= 201103L
	String(String &&o)
	{
		len = o.len;
		buf = o.buf;
		capacity = o.capacity;

		o.buf = NULL;
	}
#endif // C++11

	~String()
	{
		delete[] buf;
		buf = NULL;
	}

	int length() const
	{
		return len;
	}

	int compare(const String &o) const
	{
		return strcmp(buf, o.buf);
	}

	const char *c_str() const
	{
		return buf;
	}

	void reserve(int new_size)
	{
		ensureBufSize(new_size);
	}

	/**
	 * For faster append speed, please use this method instead of the + operator.
	 */
	void append(char ch)
	{
		if (len + 1 >= capacity)
		{
			ensureBufSize(len + 1);
		}

		buf[len++] = ch;
		buf[len] = '\0';
	}

	void append(char ch, int times)
	{
		if (len + times >= capacity)
		{
			ensureBufSize(len + times);
		}
		while (times-- > 0)
			buf[len++] = ch;
		buf[len] = '\0';
	}

	String &operator=(const String &o)
	{
		if (this == &o)
			return *this;

		len = o.len;
		if (len >= capacity)
		{
			delete[] buf;
			makeBuf(len);
		}

		strcpy(buf, o.buf);
		return *this;
	}

#if __cplusplus >= 201103L
	String &operator=(String &&o)
	{
		if (this == &o)
			return *this;

		len = o.len;
		// We free the smaller one here.
		if (o.capacity > capacity)
		{
			delete[] buf;

			buf = o.buf;
			capacity = o.capacity;

			o.buf = NULL;
		}
		else
		{
			strcpy(buf, o.buf);
		}

		return *this;
	}
#endif // C++11

	String &operator=(const char *p) throw (invalid_argument)
	{
		if (p == NULL)
			throw invalid_argument("adt::string char pointer is NULL.");

		if (buf == p)
			return *this;

		len = strlen(p);
		if (len >= capacity)
		{
			delete[] buf;
			makeBuf(len);
		}

		strcpy(buf, p);
		return *this;
	}

	String &operator+=(const String &o)
	{
		if (len + o.len >= capacity)
		{
			ensureBufSize(len + o.len);
		}

		strcpy(buf + len, o.buf);
		len += o.len;

		return *this;
	}

	String &operator+=(const char *p) throw (invalid_argument)
	{
		if (p == NULL)
			throw invalid_argument("adt::string char pointer is NULL.");

		int other_len = strlen(p);
		if (len + other_len >= capacity)
		{
			ensureBufSize(len + other_len);
		}

		strcpy(buf + len, p);
		len += other_len;

		return *this;
	}

	char &operator[](int i) throw (out_of_range)
	{
		if (i >= len)
			throw out_of_range("adt::string::operator[]");
		return buf[i];
	}

	const char &operator[](int i) const throw (out_of_range)
	{
		if (i >= len)
			throw out_of_range("adt::string::operator[] const");
		return buf[i];
	}

	friend String operator+(const String &t, const String &o);

	friend bool operator>(const String &t, const String &o);
	friend bool operator>=(const String &t, const String &o);
	friend bool operator<(const String &t, const String &o);
	friend bool operator<=(const String &t, const String &o);
	friend bool operator==(const String &t, const String &o);
	friend bool operator!=(const String &t, const String &o);

	friend istream &operator>>(istream &i, String &o);
	friend ostream &operator<<(ostream &s, const String &o);
};

}

#endif
