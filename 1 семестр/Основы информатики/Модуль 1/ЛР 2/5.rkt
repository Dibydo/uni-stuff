(define (any? pred? xs)
  (define (iter xs1)
    (and
     (not (null? xs1))
     (or
      (pred? (car xs1)) (iter (cdr xs1)))))
  (iter xs))

(define (all? pred? xs)
  (define (iter count xs1)
    (or
     (and (null? xs1) (= count (length xs)))
     (and (pred? (car xs1)) (iter (+ count 1) (cdr xs1)))))
  (iter 0 xs))

(any? odd? '(1 3 5 7))
(any? odd? '(0 1 2 3))
(any? odd? '(0 2 4 6))
(any? odd? '())

(all? odd? '(1 3 5 7))
(all? odd? '(0 1 2 3))
(all? odd? '(0 2 4 6))
(all? odd? '())