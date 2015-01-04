/*
 * string.cpp
 *
 *  Created on: 2014Äê12ÔÂ8ÈÕ
 *      Author: Administrator
 */

#include "string.h"

namespace adt
{

String operator+(const String &t, const String &o)
{
	String r = t;
	r += o;
	return r;
}

bool operator>(const String &t, const String &o)
{
	return t.compare(o) > 0;
}

bool operator<(const String &t, const String &o)
{
	return t.compare(o) < 0;
}

bool operator>=(const String &t, const String &o)
{
	return t.compare(o) > 0;
}

bool operator<=(const String &t, const String &o)
{
	return t.compare(o) < 0;
}

bool operator==(const String &t, const String &o)
{
	return t.compare(o) == 0;
}

bool operator!=(const String &t, const String &o)
{
	return t.compare(o) != 0;
}

istream &operator>>(istream &i, String &o)
{
	char ch;
	o.len = 0;
	while ((i.get(ch)) && ch != '\n')
	{
		if (o.len == o.capacity - 1)
		{
			o.ensureBufSize(o.len + 1);
		}
		o.buf[o.len++] = ch;
	}

	o.buf[o.len] = '\0';
	return i;
}

ostream &operator<<(ostream &os, const String &o)
{
#ifdef DEBUG
	return os << "[" << o.capacity << ":" << o.len << "] " << o.buf;
#else
	return os << o.buf;
#endif
}

}

