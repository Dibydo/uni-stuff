(define-syntax lazy-cons
  (syntax-rules ()
    ((_ a b) (cons a (delay b)))))

(define (lazy-car p)
  (car p))

(define (lazy-cdr p)
  (force (cdr p)))

(define (lazy-head xs n)
  (if (zero? n)
      '()
      (cons (lazy-car xs) (lazy-head (lazy-cdr xs) (- n 1)))))

(define (naturals n)
  (lazy-cons n (naturals (+ n 1))))

(define (factor inp)
  (let fac ((n inp))
    (if (zero? n)
        1
        (* n (fac (- n 1))))))

(define (factorial num)
  (lazy-cons (factor num) (factorial (+ num 1))))

(define (lazy-factorialh n)
  (lazy-head (factorial 0) (+ n 1)))

(define (lazy-factorial n)
  (lazy-car (reverse (lazy-factorialh n))))

(display (lazy-head (naturals 10) 12))
(newline)
(begin
  (display (lazy-factorial 10)) (newline)
  (display (lazy-factorial 50)) (newline))