#include <stdio.h>

int main(int args, char** argv)
{
	long n, k, x0, kf, proizv = 0;
	scanf("%ld%ld%ld", &n, &k, &x0);
	for (int i = n; i >= k; i--)
	{
		scanf("%ld", &kf);
		for (int j = 0; j < k; j++)
		{
			kf *= (i - j);
		}
		proizv += kf;
		if (i != k)
		{
			proizv *= x0;
		}
	}
	printf("%ld", proizv);
	return 0;
}