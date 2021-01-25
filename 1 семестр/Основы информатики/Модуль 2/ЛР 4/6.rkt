(define-syntax when
  (syntax-rules ()
    ((_ if-expr expr1 . expr2)
     (if if-expr
         (begin expr1 . expr2)))))

(define-syntax unless
  (syntax-rules ()
    ((_ if-expr expr1 . expr2)
     (if (not if-expr)
         (begin expr1 . expr2)))))

(define-syntax for
  (syntax-rules (as in)
    ((_ x in xs . expr) (for-each (lambda (x) (begin . expr)) xs))
    ((_ xs as x . expr) (for-each (lambda (x) (begin . expr)) xs))))

(define-syntax while
  (syntax-rules ()
    ((_ cond? . expr) (let end () (when cond? (begin . expr) (end))))))

(define-syntax repeat
  (syntax-rules (until)
    ((_ (expr1 . expr2) until cond?) (let end () (begin expr1 . expr2) (when (not cond?) (end))))))

(define-syntax cout
  (syntax-rules (<< endl)
    ((_ << endl) (newline))
    ((_ << endl . expr2) (begin (newline) (cout . expr2)))
    ((_ << expr1 . expr2) (begin (display expr1) (cout . expr2)))))

(define x 1)
(when   (> x 0) (display "x > 0")  (newline))
(unless (= x 0) (display "x != 0") (newline))
(newline)
(for i in '(1 2 3)
  (for j in '(4 5 6)
    (display (list i j))
    (newline)))
(newline)
(for '(1 2 3) as i
  (for '(4 5 6) as j
    (display (list i j))
    (newline)))
(newline)
(let ((p 0)
      (q 0))
  (while (< p 3)
         (set! q 0)
         (while (< q 3)
                (display (list p q))
                (newline)
                (set! q (+ q 1)))
         (set! p (+ p 1))))
(newline)
(let ((i 0)
      (j 0))
  (repeat ((set! j 0)
           (repeat ((display (list i j))
                    (set! j (+ j 1)))
                   until (= j 3))
           (set! i (+ i 1))
           (newline))
          until (= i 3)))
(newline)
(cout << "a = " << 1 << endl << "b = " << 2 << endl)