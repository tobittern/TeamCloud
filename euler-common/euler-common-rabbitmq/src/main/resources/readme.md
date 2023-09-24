#spring:
#  rabbitmq:
#    addresses: 82.157.98.189:5672
#    username: audit
#    password: 3rS7EJqixUJCAMjQOrif
#    virtual-host: dingduan.audit
#    listener:
#      simple:
#        concurrency: 2
#        max-concurrency: 4
#        prefetch: 2
#        retry:
#          enabled: true
#          max-attempts: 2
#          initial-interval: 3000
#          max-interval: 5000
#    publisher-confirm-type: correlated
#
#
#
#
#webconfig:
#  rabbitmq:
#    addDraftExchange: Ex_Audit_AddDraft
#    addDraftRoutingkey: Rk_Audit_AddDraft
#    addDraftQueue: Qu_Audit_AddDraft
