(define (interpret program stack)
;Флаг для сравнения и логических операций
  (define (compare-logic-flag stack sign)
    (cond
      ((eq? sign '=) (if (= (car stack) (cadr stack))
                         (cons -1 (cddr stack))
                         (cons 0 (cddr stack))))
      ((eq? sign '>) (if (< (car stack) (cadr stack))
                         (cons -1 (cddr stack))
                         (cons 0 (cddr stack))))
      ((eq? sign '<) (if (> (car stack) (cadr stack))
                         (cons -1 (cddr stack))
                         (cons 0 (cddr stack))))
      ((eq? sign 'and) (if (or (= (car stack) 0) (= (cadr stack) 0))
                           (cons 0 (cddr stack))
                           (cons -1 (cddr stack))))
      ((eq? sign 'or) (if (and (= (car stack) 0) (= (cadr stack) 0))
                          (cons 0 (cddr stack))
                          (cons -1 (cddr stack))))
      ((eq? sign 'not) (if (= (car stack) 0)
                           (cons -1 (cadr stack))
                           (cons 0 (cadr stack))))))
;
  (define (create-define program counter stack buf lexic)
    (let ((vector-elem (vector-ref program counter)))
      (if (eq? vector-elem 'end)
          (core program (+ counter 1) stack buf lexic)
          (create-define program (+ counter 1) stack buf lexic))))

  (define (prog_if counter)
    (if (eq? (vector-ref program counter) 'endif)
        counter
        (prog_if (+ counter 1))))
  
  (define (core program counter stack buf lexic)
    (if (>= counter (vector-length program))
        stack
        (let ((vector-elem (vector-ref program counter)))
          (cond
            ((number? vector-elem) (core program (+ 1 counter) (cons vector-elem stack) buf lexic))
            ((eq? vector-elem '+) (core program (+ 1 counter) (cons (+ (car stack) (cadr stack)) (cddr stack)) buf lexic))
            ((eq? vector-elem '-) (core program (+ 1 counter) (cons (- (cadr stack) (car stack)) (cddr stack)) buf lexic))
            ((eq? vector-elem '*) (core program (+ 1 counter) (cons (* (cadr stack) (car stack)) (cddr stack)) buf lexic))
            ((eq? vector-elem '/) (core program (+ 1 counter) (cons (quotient (cadr stack) (car stack)) (cddr stack)) buf lexic))
            ((eq? vector-elem 'mod) (core program (+ 1 counter) (cons (remainder (cadr stack) (car stack)) (cddr stack)) buf lexic))
            ((eq? vector-elem 'neg) (core program (+ 1 counter) (cons (* -1 (car stack)) (cdr stack)) buf lexic))
            ((or (eq? vector-elem '=) (eq? vector-elem '>) (eq? vector-elem '<) (eq? vector-elem 'not) (eq? vector-elem 'and) (eq? vector-elem 'or)) (core program (+ 1 counter) (compare-logic-flag stack vector-elem) buf lexic))
            ((eq? vector-elem 'drop) (core program (+ 1 counter) (cdr stack) buf lexic))
            ((eq? vector-elem 'swap) (core program (+ 1 counter) (cons (cadr stack) (cons (car stack) (cddr stack))) buf lexic))
            ((eq? vector-elem 'dup) (core program (+ 1 counter) (cons (car stack) stack) buf lexic))
            ((eq? vector-elem 'over) (core program (+ 1 counter) (cons (cadr stack) stack) buf lexic))
            ((eq? vector-elem 'rot) (core program (+ 1 counter) (cons (caddr stack) (cons (cadr stack) (cons (car stack) (cdddr stack)))) buf lexic))
            ((eq? vector-elem 'depth) (core program (+ 1 counter) (cons (length stack) stack) buf lexic))
            ((eq? vector-elem 'define) (create-define program (+ 1 counter) stack buf (append lexic (list (list (vector-ref program (+ 1 counter)) (+ 2 counter))))))
            ((or (eq? vector-elem 'end) (eq? vector-elem 'exit)) (core program (car buf) stack (cdr buf) lexic))
            ((eq? vector-elem 'if) (if (not (= (car stack) 0))
                                       (core program (+ counter 1) (cdr stack) buf lexic)
                                       (core program (+ (prog_if counter) 1) (cdr stack) buf lexic)))
            ((eq? vector-elem 'endif) (core program (+ 1 counter) stack buf lexic))
            (else (core program (cadr (assoc vector-elem lexic)) stack (cons (+ 1 counter) buf) lexic))))))
  (core program 0 stack '() '()))
;--ТЕСТЫ--
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
                   (test (interpret #(define abs dup 0 < if neg endif end abs) '(-9)) '(9))
                   (test (interpret #(2 3 * 4 5 * +) '()) '(26))
                   (test (interpret #(define -- 1 - end 5 -- --) '()) '(3))
                   (test (interpret #(define abs dup 0 < if neg endif end 9 abs -9 abs) (quote ())) '(9 9))
                   (test (interpret #(define =0? dup 0 = end define <0? dup 0 < end define signum =0? if exit endif <0? if drop -1 exit endif drop 1 end 0 signum -5 signum 10 signum) (quote ())) '(1 -1 0))
                   (test (interpret #(define -- 1 - end define =0? dup 0 = end define =1? dup 1 = end define factorial
                                       =0? if drop 1 exit endif
                                       =1? if drop 1 exit endif
                                       dup -- factorial * end 0 factorial 1 factorial 2 factorial 3 factorial 4 factorial) (quote ())) '(24 6 2 1 1))))
(run-tests the-tests)