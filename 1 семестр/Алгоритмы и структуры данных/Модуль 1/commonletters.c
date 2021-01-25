#include <stdio.h>

int main(int args, char** argv)
{
	unsigned long s, s1 = 0, s2 = 0;
	char el = 0;
	while ((el = getchar()) != 32)
	{
		if (el <= 90)
		{
			s1 = (s1 | (1ul << (unsigned long)(el - 65)));
		}
		else
		{
			s1 = (s1 | (1ul << (unsigned long)(el - 71)));
		}
	}
	while ((el = getchar()) != 10)
	{
		if (el <= 90)
		{
			s2 = (s2 | (1ul << (unsigned long)(el - 65)));
		}
		else
		{
			s2 = (s2 | (1ul << (unsigned long)(el - 71)));
		}
	}
	s = (s1 & s2);
	for (int i = 0; i <= 51; i++)
	{
		if (i <= 25)
		{
			if (s & (1ul << (unsigned long) i))
			{
				printf("%c", i + 65);
			}
		}
		else
		{
			if (s & (1ul << (unsigned long) i))
			{
				printf("%c", i + 71);
			}
		}
	}
	return 0;
}