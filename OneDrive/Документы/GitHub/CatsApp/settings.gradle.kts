rootProject.name = "DaddyDogs"

include("banks")
include("banks-console")
include("les-chats:data-access-layer")
findProject("les-chats:data-access-layer")?.name = "data-access-layer"
include("les-chats:presentation-layer")
findProject("les-chats:presentation-layer")?.name = "presentation-layer"
include("les-chats:service-layer")
findProject("les-chats:service-layer")?.name = "service-layer"