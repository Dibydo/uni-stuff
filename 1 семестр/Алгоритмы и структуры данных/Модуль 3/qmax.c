#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct Yep Yep;

struct Yep
{
	int* info;
	int* kac;
	int top;
	int high1;
	int high2;
};

int ste1(Yep* inp)
{
	if (inp -> high1 == 0)
		return 1;
	else return 0;
}

int ste2(Yep* inp)
{
	if (inp -> high2 == (inp -> top - 1))
		return 1;
	else return 0;
}

void push1(Yep* inp, int inpv)
{
	inp -> info[inp -> high1] = inpv;
	if (inp -> high1 == 0)
		inp -> kac[inp -> high1] = inpv;
	else
		if (inp -> kac[inp -> high1 - 1] < inpv)
			inp -> kac[inp -> high1] = inpv;
		else inp -> kac[inp -> high1] = inp -> kac[inp -> high1 - 1];
	inp -> high1++;
}

void push2(Yep* inp, int inpv)
{
	inp -> info[inp -> high2] = inpv;
	if (inp -> high2 == (inp -> top - 1))
		inp -> kac[inp -> high2] = inpv;
	else
		if (inp -> kac[inp -> high2 + 1] > inpv)
			inp -> kac[inp -> high2] = inp -> kac[inp -> high2 + 1];
		else inp -> kac[inp -> high2] = inpv;
	inp -> high2--;
}

int p1(Yep* inp)
{
	int temp;
	inp -> high1--;
	temp = inp -> info[inp -> high1];
	return temp;
}

int p2(Yep* inp)
{
	int temp;
	inp -> high2++;
	temp = inp -> info[inp -> high2];
	return temp;
}

void Enqueue(Yep* inp, int inpv)
{
	push1(inp, inpv);
}

int Dequeue(Yep* inp)
{
	int temp;
	if (ste2(inp) == 1)
	{
		while (ste1(inp) == 0)
			push2(inp, p1(inp));
	}
	temp = p2(inp);
	return temp;
}

int QueueEmpty(Yep* inp)
{
	if ((ste1(inp) == 1) && (ste2(inp) == 1))
		return 1;
	else return 0;
}

int Maximum(Yep inp)
{
	int temp;
	if ((inp.high1 != 0) && ((inp.high2 == inp.top - 1) || ((inp.high2 != inp.top - 1) && (inp.kac[inp.high1 - 1] > inp.kac[inp.high2 + 1]))))
		return inp.kac[inp.high1 - 1];
	else return inp.kac[inp.high2 + 1];
}

int main()
{
	int hh1, hh2, hh3;
	char task[5];
	scanf("%d", &hh1);
	struct Yep yy;
	yy.info = (int*)malloc(100000 * (sizeof(int) + 1));
	yy.kac = (int*)malloc(100000 * (sizeof(int) + 1));
	yy.top = 100000;
	yy.high1 = 0;
	yy.high2 = 99999;
	for (int i = 0; i < hh1; i++)
	{
		scanf("%s", task);
		if (strcmp(task, "ENQ") == 0)
		{
			scanf("%d", &hh2);
			Enqueue(&yy, hh2);
		}
		if (strcmp(task, "DEQ") == 0)
		{
			hh2 = Dequeue(&yy);
			printf("%d\n", hh2);
		}
		if (strcmp(task, "MAX") == 0)
		{
			hh3 = Maximum(yy);
			printf("%d\n", hh3);
		}
		if (strcmp(task, "EMPTY") == 0)
		{
			hh3 = QueueEmpty(&yy);
			if (hh3 == 1)
				printf("true\n");
			else printf("false\n");
		}
	}
	free(yy.info);
	free(yy.kac);
	return 0;
}