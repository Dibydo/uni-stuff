#include <stdio.h>

int main(int args, char** argv)
{
	long n, k, s = 0, smax = 0, m[100000], dopA, el;
	scanf("%ld%ld", &n, &k);
	for (int i = 0; i < k; i++)
	{
		scanf("%ld", &dopA);
		s += dopA;
		m[i] = dopA;
	}
	smax = s;
	for (int i = k; i < n; i++)
	{
		scanf("%ld", &dopA);
		s += dopA;
		s -= m[i % k];
		m[i % k] = dopA;
		if (s > smax)
		{
			smax = s;
			el = i - k + 1;
		}
	}
	printf("%ld\n", el);
	return 0;
}