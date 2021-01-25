#include <stdio.h>

int main(int args, char** argv)
{
	long am, a, b, a1, b1;
	scanf("%ld", &am);
	scanf("%ld%ld", &a, &b);
	for (int i = 1; i < am; i++)
	{
		scanf("%ld%ld", &a1, &b1);
		if ((a1 < b) && (b1 > b))
		{
			b = b1;
		}
		if (a1 > (b + 1))
		{
			printf("%ld %ld\n", a, b);
			a = a1;
			b = b1;
		}
		if (b1 > b)
		{
			b = b1;
		}
	}
	printf("%ld %ld\n", a, b);
	return 0;
}