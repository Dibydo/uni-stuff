#include <stdio.h>
#include <stdlib.h>

typedef struct x x;

struct x
{
	int* arr;
	int start;
	int end;
	int top;
	int counter;
};

struct x InitQueue (int var)
{
	struct x hh;
	hh.arr = (int*)malloc(4 * sizeof(int));
	hh.start = 0;
	hh.end = 0;
	hh.top = var;
	hh.counter = 0;
	return hh;
}

int QueueEmpty (x* hh)
{
	if (hh->counter == 0)
		return 1;
	else return 0;
}

void Enqueue (x* hh, int var)
{
	int co, hv;
	hv = hh -> top;
	if (hh -> counter == hh -> top)
	{
		hh -> start += hv;
		hh -> top *= 2;
		hh -> arr = realloc (hh -> arr, sizeof(int) * (hh -> top));
		for (co = hh -> end; co < hv; co++)
			hh -> arr[hv + co] = hh -> arr[co];
	}
	hh -> arr[hh -> end] = var;
	hh -> end += 1;
	if (hh -> end == hh -> top)
		hh -> end = 0;
	hh -> counter += 1;
}

int Dequeue (x* hh)
{
	int hv;
	hv = hh -> arr[hh -> start];
	hh -> start += 1;
	if (hh -> start == hh -> top)
		hh -> start = 0;
	hh -> counter -= 1;
	return hv;
}

int main()
{
	int hv1, hv2, hv3, i;
	char hs[5];
	struct x hst = InitQueue (4);
	scanf("%d", &hv1);
	for (i = 0; i < hv1; i++)
	{
		scanf("%s", &hs);
		if (strcmp(hs, "ENQ") == 0)
		{
			scanf("%d", &hv2);
			Enqueue(&hst, hv2);
		}
		if (strcmp(hs, "DEQ") == 0)
		{
			hv2 = Dequeue(&hst);
			printf("%d\n", hv2);
		}
		if (strcmp(hs, "EMPTY") == 0)
		{
			hv3 = QueueEmpty(&hst);
			if (hv3 == 1)
				printf("true\n");
			else printf("false\n");
		}
	}
	free (hst.arr);
	return 0;
}