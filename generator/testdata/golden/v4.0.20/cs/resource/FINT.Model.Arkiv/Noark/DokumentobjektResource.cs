using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

namespace FINT.Model.Arkiv.Noark
{

    public class DokumentobjektResource 
    {

    
        public string Filstorrelse { get; set; }
        public string FormatDetaljer { get; set; }
        public string Sjekksum { get; set; }
        public string SjekksumAlgoritme { get; set; }
        public long? Versjonsnummer { get; set; }
        
        public DokumentobjektResource()
        {
            Links = new Dictionary<string, List<Link>>();
        }

        [JsonProperty(PropertyName = "_links")]
        public Dictionary<string, List<Link>> Links { get; private set; }

        protected void AddLink(string key, Link link)
        {
            if (!Links.ContainsKey(key))
            {
                Links.Add(key, new List<Link>());
            }
            Links[key].Add(link);
        }
     
            

        public void AddFilformat(Link link)
        {
            AddLink("filformat", link);
        }

        public void AddVariantFormat(Link link)
        {
            AddLink("variantFormat", link);
        }

        public void AddOpprettetAv(Link link)
        {
            AddLink("opprettetAv", link);
        }

        public void AddReferanseDokumentfil(Link link)
        {
            AddLink("referanseDokumentfil", link);
        }
    }
}
