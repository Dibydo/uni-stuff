#include <stdio.h>
#include <stdlib.h>

typedef struct x x;
typedef struct y y;

struct x
{
	int h;
	int ind;
};

struct y
{
	int counter;
	int top;
	struct x* heap;
};

void swap (int first, int second, y* f)
{
	int h_,ind_;
	h_ = f -> heap[first].h;
	ind_ = f -> heap[first].ind;
	f -> heap[first].h = f -> heap[second].h;
	f -> heap[first].ind = f -> heap[second].ind;
	f -> heap[second].h = h_;
	f -> heap[second].ind = ind_;
}

void heapify (int count, int num, y* wh)
{
	int h1,h2,h3;
	while (1)
	{
		h1 = 2*count + 1;
		h2 = h1 + 1;
		h3 = count;
		if ((h1 < num) && (wh -> heap[count].h > wh -> heap[h1].h))
			count = h1;
		if ((h2 < num) && (wh -> heap[count].h > wh -> heap[h2].h))
			count = h2;
		if (count == h3)
			break;
		swap(count,h3,wh);
	}
}

void ins (y* wh, x h)
{
	int count;
	count = wh -> counter;
	wh -> counter += 1;
	wh -> heap[count] = h;
	while ((count > 0) && (wh -> heap[(count - 1)/2].h > wh -> heap[count].h))
	{
		swap((count - 1)/2, count, wh);
		count = (count - 1)/2;
	}
}

x extractmin(y* wh)
{
	x b = wh -> heap[0];
	wh -> counter -= 1;
	if (wh -> counter > 0)
	{
		wh -> heap[0] = wh -> heap[wh->counter];
		heapify(0, wh->counter, wh);
	}
	return b;
}

int main()
{
	int num, total;
	total = 0;
	scanf("%d", &num);
	int* h1 = malloc(sizeof(int) * (num + 1));
	int** h2 = malloc(sizeof(int*) * (num + 1));
	int h3[num];
	y* en = malloc(1 * sizeof(y));
	en -> top = num;
	en -> counter = 0;
	en -> heap = malloc((num+1) * sizeof(x));
	for (int i = 0; i < num; i++)
	{
		scanf("%d", &h1[i]);
		total += h1[i];
		h2[i] = malloc((h1[i] + 1) * sizeof(int));
		h3[i] = 0;
	}
	for (int i = 0; i < num; i++)
	{
		for (int j = 0; j<h1[i]; j++)
		{
			scanf("%d", &h2[i][j]);
		}
	}
	for (int i = 0; i < num; i++)
	{
		if (h1[i] != 0)
		{
			struct x sm;
			sm.h = h2[i][0];
			sm.ind = i;
			ins (en, sm);
			h3[i] += 1;
		}
	}
	for (int i = 0; i < total; i++)
	{
		struct x borch;
		borch = extractmin(en);
		printf("%d ", borch.h);
		if (h3[borch.ind] != h1[borch.ind])
		{
			struct x yep;
			yep.h = h2[borch.ind][h3[borch.ind]];
			yep.ind = borch.ind;
			ins(en, yep);
			h3[borch.ind]++;
		}
	}
	free(en->heap);
	for (int i = 0; i < num; i++)
		free(h2[i]);
	free(h2);
	free(h1);
	free(en);
	return 0;
}