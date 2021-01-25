#include <stdio.h>
#include <stdlib.h>

typedef union Int32 Int32;

union Int32 { 
	int x; 
	unsigned char bytes[4]; 
};

Int32* sort1(Int32* cf, int inp, int hi)
{
	int i, j;
	unsigned char zh;
	char harr[256] = {0};
	i = 0;
	j = inp - 1;
	for (i = 0; i < inp; i++)
		harr[cf[i].bytes[hi]] += 1;
	while (i < 256)
	{
		harr[i] = harr[i] + harr[i - 1];
		i += 1;
	}
	Int32* hbr = (Int32*)malloc((inp + 1) * sizeof(Int32));
	for (i = 0; i < inp; i++)
		hbr[i] = cf[i];
	while (j >= 0)
	{
		zh = cf[j].bytes[hi];
		i = harr[zh] - 1;
		harr[zh] = i;
		hbr[i].x = cf[j].x;
		j -= 1;
	}
	free(cf);
	return hbr;
}

Int32* sort2(Int32* cf, int inp, int hi)
{
	int i, j;
	unsigned char zh1, zh2;
	char harr[256] = {0};
	i = 0;
	j = inp - 1;
	for (i = 0; i < inp; i++)
		harr[128 & cf[i].bytes[hi]] += 1;
	i = 254;
	while (i >= 0)
	{
		harr[i] = harr[i] + harr[i + 1];
		i--;
	}
	Int32* hbr = (Int32*)malloc((inp + 1) * sizeof(Int32));
	for (i = 0; i < inp; i++)
		hbr[i] = cf[i];
	while (j >= 0)
	{
		zh1 = cf[j].bytes[hi];
		i = harr[128 & zh1] - 1;
		harr[128 & zh1] = i;
		hbr[i].x = cf[j].x;
		j -= 1;
	}
	free(cf);
	return hbr;
}

int main()
{
	int hh;
	scanf("%d", &hh);
	Int32* arg = (Int32*)malloc((hh + 1) * sizeof(Int32));
	for (int i = 0; i < hh; i++)
		scanf("%d", &arg[i].x);
	for (int i = 0; i <= 3; i++)
		arg = sort1(arg, hh, i);
	arg = sort2(arg, hh, 3);
	for (int i = 0; i < hh; i++)
		printf("%d ", arg[i].x);
	free(arg);
	return 0;
}