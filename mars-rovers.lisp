;;;
;;; Plateau
;;;

(defstruct plateau width height)

(defvar *plateau*)

(defun allowed-position (x y)
  (and (<= 0 x (plateau-width *plateau*))
       (<= 0 y (plateau-height *plateau*))))

(defun do-plateau (width height)
  (setf *plateau*
	(make-plateau :width (parse-integer width)
		      :height (parse-integer height))))

;;;
;;; Rover
;;;

(defstruct rover x y orientation)

(defun rover-move (rover dx dy)
  (let ((next-x (+ (rover-x rover) dx))
	(next-y (+ (rover-y rover) dy)))
    (when (allowed-position next-x next-y)
      (setf (rover-x rover) next-x
	    (rover-y rover) next-y))))

(defun rover-move-forward (rover)
  (case (rover-orientation rover)
    (0 (rover-move rover 0 1))
    (1 (rover-move rover 1 0))
    (2 (rover-move rover 0 -1))
    (3 (rover-move rover -1 0))))

(defun rover-turn-left (rover)
  (setf (rover-orientation rover)
	(rem (+ (rover-orientation rover) 3) 4)))

(defun rover-turn-right (rover)
  (setf (rover-orientation rover)
	(rem (+ (rover-orientation rover) 1) 4)))

(defvar *rover* nil)

(defun do-rover (x y orientation)
  (setf *rover*
	(make-rover :x (parse-integer x)
		    :y (parse-integer y)
		    :orientation (position (char orientation 0) "NESW"))))

;;;
;;; Program
;;;

(defun perform-command (command)
  (case command
    (#\M (rover-move-forward *rover*))
    (#\L (rover-turn-left *rover*))
    (#\R (rover-turn-right *rover*))))

(defun do-program (program)
  (loop for command across program
	do (perform-command command))
  (format t "~A ~A ~A~%"
	  (rover-x *rover*)
	  (rover-y *rover*)
	  (char "NESW" (rover-orientation *rover*)))
  *rover*)

(defun split-by-one-space (string)
  (loop for i = 0 then (1+ j)
	as j = (position #\Space string :start i)
	collect (subseq string i j)
	while j))

(defun reader (stream char)
  (let ((line (read-line stream nil stream)))
    (if (eq line stream)
	nil
        (let ((parts (split-by-one-space (format nil "~A~A" char line))))
	  (case (length parts)
	    (1 (do-program (first parts)))
	    (2 (do-plateau (first parts) (second parts)))
	    (3 (do-rover (first parts) (second parts) (third parts))))))))

(let ((rt (copy-readtable)))
  (dotimes (i 17)
    (set-macro-character (char "0123456789NEWSMLR" i) #'reader nil rt))

  (defun rover-syntax ()
    (setf *readtable* rt))

  (defun lisp-syntax ()
    (setf *readtable* (copy-readtable nil))))

(rover-syntax)
(load "input.txt")

#|

5 5
1 2 N
LMLMLMLMM
3 3 E
MMRMMRMRRM
|#

