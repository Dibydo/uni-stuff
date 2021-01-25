import sys


def readDictFile(filename):
    allwords = []
    inpfile = open(filename, "r")
    for line in inpfile:
        pureWord = line.strip()
        allwords.append(pureWord.lower())
    inpfile.close()
    return allwords


def readTextFile(filename):
    allwords = []
    inpfile = open(filename, "r")
    for line in inpfile:
        lineWords = line.strip().split()
        for word in lineWords:
            allwords.append(word.strip(".,!\":;?").lower())
    inpfile.close()
    return allwords


def findErrors(dictionaryWords, textWords):
    wrongWords = []
    for word in textWords:
        if word not in dictionaryWords:
            wrongWords.append(word)
    return wrongWords


def printErr(err_list):
    print("Misspelled words:")
    for word in err_list:
        print(word)


def main():
    dictFile = sys.argv[1]
    textFile = sys.argv[2]
    print("Введенный файл-словарь: %s" % dictFile)
    print("Введенный текст для проверки: %s" % textFile)
    dictList = readDictFile(dictFile)
    textList = readTextFile(textFile)
    errorList = findErrors(dictList, textList)
    printErr(errorList)


main()
