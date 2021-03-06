(define (my-range a b d)
  (if (< a b)
      (cons a (my-range (+ d a) b d))
      '()
      )
  )
(define (my-flatten xs)
  (if (not (null? xs))
      (begin
        (if (pair? (car xs))
            (append (my-flatten (car xs)) (my-flatten (cdr xs)))
            (cons (car xs) (my-flatten (cdr xs)))))
      '()
      )
  )
(define (my-element? x xs)
  (if (null? xs)
      (= 1 0)
      (if (equal? x (car xs))
          (= 1 1)
          (my-element? x (cdr xs))
          )
      )
  )
(define (my-filter pred? xs)
  (if (null? xs)
      '()
      (begin
        (if (pred? (car xs))
            (cons (car xs) (my-filter pred? (cdr xs)))
            (my-filter pred? (cdr xs))))
      )
  )
(define (my-fold-left op xs)
  (if (not (null? (cdr xs)))
      (my-fold-left op (cons (op (car xs) (car (cdr xs))) (cdr (cdr xs))))
      (car xs)
      )
  )
(define (my-fold-right op xs)
  (if (not (null? (cdr xs)))
      (my-fold-right op (reverse (cons (op (car (reverse xs)) (car (cdr (reverse xs)))) (cdr (cdr (reverse xs))))))
      (car xs)
      )
  )