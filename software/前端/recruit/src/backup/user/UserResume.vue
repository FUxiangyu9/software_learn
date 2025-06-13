<script setup>
import VuePdfEmbed from 'vue-pdf-embed'
// essential styles
import 'vue-pdf-embed/dist/style/index.css'

// optional styles
import 'vue-pdf-embed/dist/style/annotationLayer.css'
import 'vue-pdf-embed/dist/style/textLayer.css'

import {ref} from 'vue'
// either URL, Base64, binary, or document proxy
// const pdfSource = 'https://mozilla.github.io/pdf.js/web/compressed.tracemonkey-pldi-09.pdf'
const resumeInfo = ref([{
    "id":'',
    "uid":'',
    "resumeUrl":''
}])
import {userResumeListService} from "@/api/user.js"
const getUserResume=async()=>{
    let result = await userResumeListService();
    resumeInfo.value = result.data;
}
getUserResume();

</script>

<template>
  <VuePdfEmbed annotation-layer text-layer :source="resumeInfo.resumeUrl" />
</template>