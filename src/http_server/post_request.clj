(ns http-server.post-request
  (:require (http-server [headers :refer :all])))

(def post-content "Temporary POST Request Content")

(defn get-script-output [root-directory requested-file]
  (let [process (.exec (java.lang.Runtime/getRuntime) (str root-directory requested-file))]
    (with-open [reader (java.io.BufferedReader. (java.io.InputStreamReader. (.getInputStream process)))]
      (loop [line (.readLine reader) body line]
        (if (not (.ready reader))
          body
          (recur (str body line) (.readLine reader)))))))


(defn handle-post-request [out-stream root-directory request]
  (let [body (get-script-output root-directory request)]
    (send-headers out-stream {:status (get-status-header :ok)
                              :type (get-type-header "txt")
                              :content-length (str "Content-Length: " (.length body))
                              :body body})))
