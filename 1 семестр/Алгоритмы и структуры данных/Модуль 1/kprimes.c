#include <stdio.h>
#include <math.h>

int main()
{
	long long sm, l, temp;
	l = 0;
	scanf("%lld", &sm);
	temp = sqrt(sm);
	char hw[temp];
	hw[0] = 0;
	hw[1] = 0;
	for (int i = 2; i < temp; i++)
		hw[i] = 1;
	for (int i = 2; i * i <= temp; i++)
	{
		if (hw[i] != 0)
		{
			for (int j = i * i; j <= temp; j += i)
				hw[j] = 0;
		}
	}
	for (int i = 0; i < temp; i++)
	{
		if ((hw[i] != 0) && (sm % i == 0))
		{
			sm /= i;
			i = 0;
		}
	}
	if (l == 0)
		l = sm;
	if (sm == 4)
		l = 2;
	printf("%lld\n", l);
	return 0;
}