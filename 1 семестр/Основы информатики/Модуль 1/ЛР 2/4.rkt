(define (intersperse e xs)
  (cond
    ((null? xs) xs)
    ((not (null? (cdr xs))) (cons (car xs) (append (cons e '()) (intersperse e (cdr xs)))))
    (else xs)))

(intersperse 'x '(1 2 3 4))
(intersperse 'x '(1 2))
(intersperse 'x '(1))
(intersperse 'x '())