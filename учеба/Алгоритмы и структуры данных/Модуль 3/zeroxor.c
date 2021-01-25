#include <stdio.h>
#include <stdlib.h>
#include <math.h>

typedef struct Yep Yep;

struct Yep
{
	int eer;
	int kf;
	Yep* bel;
};

Yep* LS(Yep* arr, int arg)
{
	Yep* harr = arr;
	if (harr == NULL)
		return 0;
	while (harr != NULL && (harr -> kf != arg))
		harr = harr -> bel;
	return harr;
}

void ins_op(struct Yep* arr, int arg)
{
	struct Yep* harr = calloc(1, sizeof(Yep));
	harr -> eer == (arg)? 0: 1;
	harr -> kf = arg;
	harr -> bel = arr -> bel;
	arr -> bel = harr;
}

void m_free(Yep** arr)
{
	for (long i = 0; i < 100000; i++)
	{
		Yep* hh1 = arr[i];
		for (;hh1 != NULL;)
		{
			Yep* hh2 = hh1;
			hh1 = hh1 -> bel;
			free(hh2);
		}
		free(hh1);
	}
	free(arr);
}

int main()
{
	int hh1, hh2, hh3, hh4;
	hh3 = 0;
	hh4 = 0;
	Yep** arr = calloc(100000, sizeof(Yep*)), *hv1;
	scanf("%d", &hh1);
	for (int i = 0; i < 100000; i++)
	{
		arr[i] = calloc(1, sizeof(Yep));
		arr[i] -> kf = 0;
		arr[i] -> eer = 0;
		arr[i] -> bel = NULL;
	}
	for (int i = 0; i < hh1; i++)
	{
		scanf("%d", &hh2);
		hh4 ^= hh2;
		hv1 = LS(arr[abs(hh4) % 100000], hh4);
		if (hv1 != NULL)
			hv1 -> eer++;
		else ins_op(arr[abs(hh4) % 100000], hh4);
	}
	hh2 = 0;
	for (int i = 0; i < 100000; i++)
	{
		hv1 = arr[i];
		while (hv1 != NULL)
		{
			int hv2 = hv1 -> eer;
			hh3 += hv2 * (hv2 + 1) / 2;
			hv1 = hv1 -> bel;
		}
	}
	printf("%d", hh3);
	m_free(arr);
	return 0;
}