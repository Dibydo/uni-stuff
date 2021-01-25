(define (derivative arg)
  (define (addition_repres f s)
    (cond
      ((equal? f 0) s)
      ((equal? s 0) f)
      (else (list '+ f s))))
  (define (subtraction_repres f s)
    (cond
      ((equal? f 0) (list '* -1 s))
      ((equal? s 0) f)
      (else (list '- f s))))
  (define (multi_repres f s)
    (cond
      ((or (equal? f 0) (equal? s 0)) 0)
      ((equal? f 1) s)
      ((equal? s 1) f)
      (else (list '* f s))))
  (define (merge_lists xs1 xs2)
    (if (null? xs2)
        xs1
        (merge_lists (append xs1 (cons (car xs2) '())) (cdr xs2))))
  (define (merge_-1 xs1 xs2)
    (cond ((null? xs2) xs1)
          ((number? (car xs2)) (merge_-1 (append xs1 (cons (list '/ 1 (car xs2)) '())) (cdr xs2)))
          ((symbol? (car xs2)) (merge_-1 (append xs1 (cons (car xs2) '())) (cdr xs2)))
          ((eq? (car (car xs2)) 'expt) (merge_-1 (append xs1 (cons (list 'expt (cadr (car xs2)) (* -1 (caddr (car xs2)))) '())) (cdr xs2)))))
  
  (cond
    ((null? arg) '())

    ((number? arg) 0)

    ((and (not (symbol? arg)) (not (< (length arg) 3)) (number? (cadr arg)) (number? (caddr arg))) 0)

    ((symbol? arg) (if (equal? (car (string->list (symbol->string arg))) #\-)
                       -1
                       1))

    ((eq? (car arg) '+) (addition_repres (derivative (cadr arg)) (derivative (caddr arg))))

    ((and (eq? (car arg) '*) (equal? (length arg) 3)) (addition_repres (multi_repres (derivative (cadr arg)) (caddr arg)) (multi_repres (cadr arg) (derivative (caddr arg)))))

    ((and (equal? (car arg) '-)) (subtraction_repres (derivative (cadr arg)) (derivative (caddr arg))))

    ((eq? (car arg) 'sin) (multi_repres (derivative (cadr arg)) (list 'cos (cadr arg))))

    ((eq? (car arg) 'cos) (multi_repres (derivative (cadr arg)) (list '* -1 (list 'sin (cadr arg)))))

    ((and (eq? (car arg) 'expt) (symbol? (cadr arg)) (not (eq? (cadr arg) 'exp))) (multi_repres (derivative (cadr arg)) (list '* (caddr arg) (list 'expt (cadr arg) (- (caddr arg) 1)))))

    ((and (eq? (car arg) 'expt) (not (eq? (cadr arg) 'exp)) (symbol? (caddr arg))) (multi_repres (derivative (caddr arg)) (list '* arg (list 'ln (cadr arg)))))

    ((and (eq? (car arg) 'e)) (multi_repres (derivative (cadr arg)) arg))

    ((eq? (car arg) 'ln) (multi_repres (derivative (cadr arg)) (list '/ 1 (cadr arg))))

    ((and (eq? (car arg) '*) (> (length arg) 3)) (addition_repres (multi_repres (derivative (cadr arg)) (merge_lists (list '*) (cddr arg))) (multi_repres (cadr arg) (derivative (merge_lists (list '*) (cddr arg))))))

    ((and (eq? (car arg) '/) (symbol? (caddr arg))) (if (equal? (cadr arg) 1)
                                                        (list '* -1 (list 'expt (caddr arg) -1))
                                                        (list '* (cadr arg) (list '* -1 (list 'expt (caddr arg) -1)))))

    ((and (eq? (car arg) '/) (not (symbol? (caddr arg))) (not (number? (caddr arg)))) (list '* (cadr arg) (derivative (merge_-1 '() (caddr arg)))))))

(define (run-test the-test)
  (let ((expr (car the-test)))
    (write expr)
    (let* ((result (eval expr (interaction-environment)))
           (status (equal? (cadr the-test) result)))
      (if status
          (display " ok")
          (display " FAIL"))
      (newline)
      (if (not status)
          (begin (display " Expected: ") (write (cadr the-test)) (newline)
                 (display " Returned: ") (write result) (newline)))
      status)))

(define (run-tests the-tests)
  (define (really x xs)
    (if (null? xs)
        x
        (really (and x (car xs)) (cdr xs))))
  (really #t (map run-test the-tests)))

(define-syntax test
  (syntax-rules ()
    ((_ expr expected-result) (list (quote expr) expected-result))))

(define the-tests (list
                   (test (derivative 2) 0)
                   (test (derivative 'x) 1)
                   (test (derivative '-x) -1)
                   (test (derivative '(* 1 x)) 1)
                   (test (derivative '(* -1 x)) -1)
                   (test (derivative '(* -4 x)) -4)
                   (test (derivative '(* 10 x)) 10)
                   (test (derivative '(- (* 2 x) 3)) 2)
                   (test (derivative '(expt x 10)) '(* 10 (expt x 9)))
                   (test (derivative '(* 2 (expt x 5))) '(* 2 (* 5 (expt x 4))))
                   (test (derivative '(expt x -2)) '(* -2 (expt x -3)))
                   (test (derivative '(expt 5 x)) '(* (expt 5 x) (ln 5)))
                   (test (derivative '(cos x)) '(* -1 (sin x)))
                   (test (derivative '(sin x)) '(cos x))
                   (test (derivative '(e x)) '(e x))
                   (test (derivative '(* 2 (e x))) '(* 2 (e x)))
                   (test (derivative '(* 2 (e (* 2 x)))) '(* 2 (* 2 (e (* 2 x)))))
                   (test (derivative '(ln x)) '(/ 1 x))
                   (test (derivative '(* 3 (ln x))) '(* 3 (/ 1 x)))
                   (test (derivative '(+ (expt x 3) (expt x 2))) '(+ (* 3 (expt x 2)) (* 2 (expt x 1))))
                   (test (derivative '(- (* 2 (expt x 3)) (* 2 (expt x 2)))) '(- (* 2 (* 3 (expt x 2))) (* 2 (* 2 (expt x 1)))))
                   (test (derivative '(/ 3 x)) '(* 3 (* -1 (expt x -1))))
                   (test (derivative '(/ 3 (* 2 (expt x 2)))) '(* 3 (* (/ 1 2) (* -2 (expt x -3)))))
                   (test (derivative '(* 2 (sin x) (cos x))) '(* 2 (+ (* (cos x) (cos x)) (* (sin x) (* -1 (sin x))))))
                   (test (derivative '(* 2 (e x) (sin x) (cos x))) '(* 2 (+ (* (e x) (* (sin x) (cos x))) (* (e x) (+ (* (cos x) (cos x)) (* (sin x) (* -1 (sin x))))))))
                   (test (derivative '(sin (* 2 x))) '(* 2 (cos (* 2 x))))
                   (test (derivative '(sin (ln (expt x 2)))) '(* (* (* 2 (expt x 1)) (/ 1 (expt x 2))) (cos (ln (expt x 2)))))
                   (test (derivative '(cos (* 2 (expt x 2)))) '(* (* 2 (* 2 (expt x 1))) (* -1 (sin (* 2 (expt x 2))))))
                   (test (derivative '(+ (sin (* 2 x)) (cos (* 2 (expt x 2))))) '(+ (* 2 (cos (* 2 x))) (* (* 2 (* 2 (expt x 1))) (* -1 (sin (* 2 (expt x 2)))))))
                   (test (derivative '(* (sin (* 2 x)) (cos (* 2 (expt x 2))))) '(+ (* (* 2 (cos (* 2 x))) (cos (* 2 (expt x 2)))) (* (sin (* 2 x)) (* (* 2 (* 2 (expt x 1))) (* -1 (sin (* 2 (expt x 2))))))))))
(run-tests the-tests)
