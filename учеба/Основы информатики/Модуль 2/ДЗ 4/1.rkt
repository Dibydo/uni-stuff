(define memoized-factorial
  (let ((var '()))
    (lambda (num)
      (let ((memoized (assq num var)))
        (if (not (equal? memoized #f))
            (cadr memoized)
            (let ((new_var
                   (if (< num 1)
                       1
                       (* num (memoized-factorial (- num 1))))))
              (set! var (cons (list num new_var) var))
              new_var))))))

(begin
  (display (memoized-factorial 10)) (newline)
  (display (memoized-factorial 50)) (newline))