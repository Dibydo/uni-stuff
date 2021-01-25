(define (sign? x)
  (member x '(#\+ #\- #\* #\/ #\^)))

(define (print-numeric str)
  (define (p str1 str2)
    (cond
      ((null? str1) str1)
      ((char-numeric? (car str1)) (p (cdr str1) (cdr str2)))
      ((equal? (car str1) #\E) (p (cdr str1) (cdr str2)))
      ((equal? (car str1) #\e) (p (cdr str1) (cdr str2)))
      ((and
        (member (car str1) '(#\+ #\-))
        (or
         (equal? (car str2) #\e)
         (equal? (car str2) #\E))) (p (cdr str1) (cdr str2)))
      ((equal? (car str1) #\.) (p (cdr str1) (cdr str2)))
      (else str1)))
  (p str (append '(#\() str)))

(define (print-var str)
  (define (subf input)
    (cond
      ((null? input) input)
      ((char-alphabetic? (car input)) (print-var (cdr input)))
      (else input)))
  (subf str))

(define (string-op str f s)
  (define (func x y z output count)
    (cond
      ((null? x) (reverse output))
      ((and (equal? (car x) y) (= count 0)) (func (cdr x) y z (cons (car x) '()) 1))
      ((and (equal? x z) (= count 1)) (func '() y z output 1))
      ((not (null? output)) (func (cdr x) y z (cons (car x) output) 1))))
  (func str f s '() 0))

(define (tokenize str)
  (define (sub input output)
    (cond
      ((null? input) (reverse output))
      ((sign? (car input)) (sub (cdr input) (cons (string->symbol (string (car input))) output)))
      ((char-numeric? (car input)) (sub (print-numeric input) (cons (string->number (list->string (string-op input (car input) (print-numeric input)))) output)))
      ((or (equal? (car input) #\() (equal? (car input) #\))) (sub (cdr input) (cons (string (car input)) output)))
      ((char-alphabetic? (car input)) (sub (print-var input) (cons (string->symbol (list->string (string-op input (car input) (print-var input)))) output)))
      ((char-whitespace? (car input)) (sub (cdr input) output))
      (else #f)))
  (sub (string->list str) '()))

(tokenize "1")
(tokenize "-a")
(tokenize "-a + b * x^2 + dy")
(tokenize "(a - 1)/(b + 1)")